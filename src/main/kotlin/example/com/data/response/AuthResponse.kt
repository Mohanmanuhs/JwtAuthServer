package example.com.data.response

import kotlinx.serialization.Serializable

// in successful sign in
@Serializable
data class AuthResponse(
    val token:String
)