package example.com.di

import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import example.com.data.user.MongoUserDataSource
import example.com.data.user.UserDataSource
import example.com.security.hashing.HashingService
import example.com.security.hashing.SHA256HashingService
import example.com.security.token.JwtTokenService
import example.com.security.token.TokenConfig
import example.com.security.token.TokenService
import org.koin.dsl.module

val mongoPw: String = System.getenv("MONGO_PW")
const val dbName = "my_db"

val appModule = module {
    single<MongoDatabase> {
        MongoClient.create(
            "mongodb+srv://mohanmanuhs9:$mongoPw@cluster0.uouah8w.mongodb.net/?retryWrites=true&w=majority&appName=Cluster0"
        ).getDatabase(dbName)
    }
    single<UserDataSource> {
        MongoUserDataSource(get())

    }
    single<HashingService> {
        SHA256HashingService()
    }
    single<TokenService> {
        JwtTokenService()
    }
    single<TokenConfig> {
        TokenConfig(
            issuer = "https://0.0.0.0:8081",
            audience = "users",
            expiresIn = 365L * 1000L * 60L * 60L * 24L,
            secret = System.getenv("JWT_SECRET")
        )
    }
}