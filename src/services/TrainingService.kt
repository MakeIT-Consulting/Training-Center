package de.makeit.ai.services

import com.ibm.cloud.sdk.core.security.IamAuthenticator
import com.ibm.watson.assistant.v1.Assistant
import com.ibm.watson.assistant.v1.model.MessageInput
import com.ibm.watson.assistant.v1.model.MessageOptions
import de.makeit.ai.data.TrainingResponse
import de.makeit.ai.data.TrainingValue
import de.makeit.klib.mole.data.filter.MoleDataFilter
import de.makeit.klib.mole.services.MoleService
import kotlinx.coroutines.delay
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TrainingService : MoleService<TrainingValue>("training_center", "training_value") {
    private val log: Logger = LoggerFactory.getLogger(this::javaClass.name)

    /* Add the MoleDataService on this class to be able to save the incoming files and delete them once the method is done */
    suspend fun watsonRequest(
        chatbotKey: String,
        skillId: String,
        apiEndpoint: String,
        assistantKey: String,
        assistantVersion: String,
        intentExample: String
    ): TrainingResponse {
        val trainingValue = TrainingValue(chatbotKey, skillId, apiEndpoint, assistantKey, assistantVersion, intentExample)
        add(trainingValue)
        var isTraining = true
        var trainingCode = 102
        var message = "started"
        val authenticator = IamAuthenticator(assistantKey)
        val assistant = Assistant(assistantVersion, authenticator)
        assistant.serviceUrl = apiEndpoint

        /* build input */
        val input = MessageInput()
        input.text = intentExample

        /*  build request option */
        val options: MessageOptions = MessageOptions.Builder(skillId)
            .input(input)
            .build()

        var confidence = 0.0
        var counter = 0

        while (confidence <= 0.7) {
            delay(1000)
            val response = assistant.message(options).execute()
            val errorStatusCode = listOf(400, 401, 403, 404, 500, 504)
            if (errorStatusCode.contains(response.statusCode) || counter == 5) {
                fallback(trainingValue)
                isTraining = false
                trainingCode = 408
                message = "failed"
                break
            }
            confidence = response.result.intents[0].confidence()
            counter++
        }

        return if (confidence > 0.7) {
            isTraining = false
            message = "success"
            trainingCode = 200
            delete(trainingValue)
            TrainingResponse(trainingCode, isTraining, confidence, message)
        } else
            TrainingResponse(trainingCode, isTraining, confidence, message)
    }

    private fun fallback(trainingValue: TrainingValue) {
        delete(trainingValue)
        log.error("error")
    }

    fun getStatus(chatbotKey: String): TrainingValue? {
        val filter = MoleDataFilter(mapOf("chatbot_key" to chatbotKey))
        return findOne(filter)
    }
}