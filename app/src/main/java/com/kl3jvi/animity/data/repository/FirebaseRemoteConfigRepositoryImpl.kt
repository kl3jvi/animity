package com.kl3jvi.animity.data.repository

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.kl3jvi.animity.R
import com.kl3jvi.animity.domain.repositories.FirebaseRemoteConfigRepository
import com.kl3jvi.animity.utils.logError
import javax.inject.Inject

class FirebaseRemoteConfigRepositoryImpl @Inject constructor(
    override val instance: FirebaseRemoteConfig
) : FirebaseRemoteConfigRepository {

    /**
     * It fetches the remote config values from the Firebase console and sets the default values.
     */
    override fun init() {
        instance.setDefaultsAsync(R.xml.root_preferences)
        instance.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.e("Successfully Retrieved", "Firebase Config Url")
            } else {
                logError(task.exception)
            }
        }
    }

    /**
     * It returns the value of the key "BASE_URL" from the instance of the [FirebaseRemoteConfig].
     */
    override fun getBaseUrl() = instance.getString("BASE_URL")
}
