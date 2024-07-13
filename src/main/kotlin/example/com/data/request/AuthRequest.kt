package example.com.data.request

import kotlinx.serialization.Serializable


// user want to sign in or sign up by using name and password
@Serializable
data class AuthRequest(
    val username: String,
    val password: String
)
