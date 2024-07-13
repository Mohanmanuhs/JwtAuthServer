package example.com.plugins

import example.com.di.appModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun Application.configureKoin() {
    install(Koin){
        slf4jLogger(level = org.koin.core.logger.Level.ERROR)
        modules(appModule)
    }
}