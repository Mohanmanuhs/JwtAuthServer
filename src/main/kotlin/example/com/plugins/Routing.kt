package example.com.plugins

import example.com.data.user.UserDataSource
import example.com.routes.authenticate
import example.com.routes.getSecretInfo
import example.com.routes.signIn
import example.com.routes.signUp
import example.com.security.hashing.HashingService
import example.com.security.token.TokenConfig
import example.com.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.get

fun Application.configureRouting(
    userDataSource: UserDataSource=get(),
    hashingService: HashingService=get(),
    tokenConfig: TokenConfig=get(),
    tokenService: TokenService=get()
) {
    routing {
        get("welcome") {
            call.respondText("hai welcome")
        }
        signUp(hashingService,userDataSource)
        signIn(hashingService,userDataSource,tokenService,tokenConfig)
        authenticate()
        getSecretInfo()
    }
}
