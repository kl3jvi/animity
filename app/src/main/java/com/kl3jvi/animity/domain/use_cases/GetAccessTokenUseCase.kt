package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.repository.activity_repositories.LoginRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetAccessTokenUseCase @Inject constructor(
    private val repository: LoginRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    /**
     * It returns a Flow of AccessToken from the repository, and it runs on the ioDispatcher
     *
     * @param grantType The type of grant you're requesting. For this, it's authorization_code.
     * @param clientId The client ID of the application.
     * @param clientSecret The client secret is a secret known only to your application and the
     * authorization server. It protects your resources by only granting tokens to authorized
     * requesters.
     * @param redirectUri The redirect URI you set in your app's settings.
     * @param authorizationToken The authorization token that was returned from the authorization
     * request.
     */
    operator fun invoke(
        grantType: String,
        clientId: Int,
        clientSecret: String,
        redirectUri: String,
        authorizationToken: String
    ) = repository.getAccessToken(
        grantType,
        clientId,
        clientSecret,
        redirectUri,
        authorizationToken
    ).flowOn(ioDispatcher)
}