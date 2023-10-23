package com.kl3jvi.animity.ui.activities.player

import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.SessionManagerListener

class SessionManager(private val callback: CastSessionCallback) {
    val sessionManagerListener: SessionManagerListener<CastSession> =
        object : SessionManagerListener<CastSession> {
            override fun onSessionStarted(
                session: CastSession,
                sessionId: String,
            ) {
                callback.onSessionStarted(session, sessionId)
            }

            override fun onSessionEnded(
                session: CastSession,
                errorCode: Int,
            ) {
                callback.onSessionEnded(session, errorCode)
            }

            override fun onSessionResumed(
                session: CastSession,
                wasSuspended: Boolean,
            ) {
                // Handle resumed sessions, e.g. if the user disconnected but reconnected
                callback.onSessionStarted(session, session.sessionId)
            }

            // Override other methods as per your need and call corresponding methods of your callback.

            override fun onSessionStarting(session: CastSession) {}

            override fun onSessionEnding(session: CastSession) {}

            override fun onSessionResumeFailed(
                session: CastSession,
                error: Int,
            ) {}

            override fun onSessionResuming(
                session: CastSession,
                sessionId: String,
            ) {}

            override fun onSessionStartFailed(
                session: CastSession,
                error: Int,
            ) {}

            override fun onSessionSuspended(
                session: CastSession,
                reason: Int,
            ) {}
        }
}

interface CastSessionCallback {
    fun onSessionStarted(
        session: CastSession,
        sessionId: String?,
    )

    fun onSessionEnded(
        session: CastSession,
        errorCode: Int,
    )
}
