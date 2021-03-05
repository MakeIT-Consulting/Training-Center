package de.makeit.ai

import de.makeit.ai.router.training
import de.makeit.klib.mole.config.MoleConfig
import io.ktor.application.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    MoleConfig.init(environment.config.property("services").getString(), null)

    routing {
        route("api/") {
            training()
        }
    }
}

