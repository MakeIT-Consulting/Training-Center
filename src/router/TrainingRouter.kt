package de.makeit.ai.router

import de.makeit.ai.services.TrainingService
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Route.training() {
    val log: Logger = LoggerFactory.getLogger(this::javaClass.name)
    val trainingService = TrainingService()
    route("training/") {
        post("start") {
            val params: HashMap<String, Any>? = call.receiveOrNull()
            if (params == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Missing Request Body")
                )
                return@post
            }
            val chatbotKey = params["chatbot_key"].toString()
            val skillId = params["skill_id"].toString()
            val apiEndpoint = params["api_endpoint"].toString()
            val assistantKey = params["assistant_key"].toString()
            val assistantVersion = params["assistant_version"].toString()
            val intentExample = params["intent_example"].toString()

            try {
                log.debug("Starting the watson training")
                val trainingResponse = trainingService.watsonRequest(chatbotKey, skillId, apiEndpoint, assistantKey, assistantVersion, intentExample)
                call.respond(HttpStatusCode.OK, trainingResponse)
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.NotFound, mapOf("error" to "Something went wrong")
                )
                return@post
            }
        }
        post("status") {
            val params: HashMap<String, Any>? = call.receiveOrNull()
            if (params == null || !params.containsKey("chatbot_key")) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("error" to "Missing Request Body")
                )
                return@post
            }
            val chatbotKey = params["chatbot_key"].toString()
            try {
                val trainingValue = trainingService.getStatus(chatbotKey)
                if (trainingValue != null) {
                    call.respond(HttpStatusCode.Processing, "Watson is still training!")
                } else
                    call.respond(HttpStatusCode.OK, "Training is done!")
            } catch (e: Exception) {
                call.respond(
                    HttpStatusCode.NotFound, mapOf("error" to "Something went wrong")
                )
                return@post
            }
        }
    }
}