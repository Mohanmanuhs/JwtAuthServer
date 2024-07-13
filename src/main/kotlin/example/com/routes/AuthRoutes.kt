package example.com.routes

import example.com.data.request.AuthRequest
import example.com.data.response.AuthResponse
import example.com.data.user.User
import example.com.data.user.UserDataSource
import example.com.security.hashing.HashingService
import example.com.security.hashing.SaltedHash
import example.com.security.token.TokenClaim
import example.com.security.token.TokenConfig
import example.com.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signUp(
    hashingService: HashingService,
    userDataSource: UserDataSource
) {
    post("signup") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val areFieldsBlank = request.username.isBlank() || request.password.isBlank()
        val isPwTooShort = request.password.length < 8
        if (isPwTooShort || areFieldsBlank) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }
        if(userDataSource.getUserByUserName(request.username)!=null){
            call.respond(HttpStatusCode.Conflict, "user with username exist")
            return@post
        }
        val saltedHash = hashingService.generateSaltedHash(request.password)
        val user = User(
            userName = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt
        )
        val wasAcknowledged = userDataSource.insertUser(user)
        if (!wasAcknowledged) {
            call.respond(HttpStatusCode.Conflict)
            return@post
        }
        call.respond(HttpStatusCode.OK)

    }
}

fun Route.signIn(
    hashingService: HashingService,
    userDataSource: UserDataSource,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    post("signIn") {
        val request = call.receiveNullable<AuthRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        val user = userDataSource.getUserByUserName(request.username)
        if (user == null) {
            call.respond(HttpStatusCode.Conflict, "Incorrect username")
            return@post
        }
        val isValidPassword = hashingService.verify(request.password, SaltedHash(hash = user.password, salt =  user.salt))
        if (!isValidPassword) {
            call.respond(HttpStatusCode.Conflict, "Incorrect password")
            return@post
        }
        val token = tokenService.generate(
            config = tokenConfig,
            TokenClaim(
                name = "userId",
                value = user.id.toString()
            )
        )
        call.respond(HttpStatusCode.OK, AuthResponse(token))
    }
}
fun Route.authenticate() {
    authenticate{
        get("authenticate") {
            call.respond(HttpStatusCode.OK)
        }
    }
}
fun Route.getSecretInfo() {
    authenticate{
        get("secret") {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId",String::class)
            call.respond(HttpStatusCode.OK,"Your userId is $userId")
        }
    }
}