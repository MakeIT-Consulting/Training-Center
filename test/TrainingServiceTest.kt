package de.makeit.ai

import de.makeit.ai.services.TrainingService
import de.makeit.klib.mole.config.MoleConfig
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull


class TrainingServiceTest {
    init {
        MoleConfig.init(
            "{" +
                    "  \"databases-for-mongodb\": [" +
                    "    {" +
                    "      \"credentials\": {" +
                    "        \"connection\": {" +
                    "          \"mongodb\": {" +
                    "            \"authentication\": {" +
                    "              \"method\": \"none\"," +
                    "              \"password\": \"\"," +
                    "              \"username\": \"\"" +
                    "            }," +
                    "            \"hosts\": [" +
                    "              {" +
                    "                \"hostname\": \"127.0.0.1\"," +
                    "                \"port\": 27017" +
                    "              }" +
                    "            ]," +
                    "            \"query_options\": {" +
                    "              \"authSource\": \"admin\" " +
                    "            } " +
                    "          }" +
                    "        } " +
                    "      } " +
                    "    }" +
                    "  ]" +
                    "}",
            null
        )
    }

    private val trainingService = TrainingService()

    @Test
    fun trainingServiceTest() {
        val chatbotKey = "AVBIJUB-C6SSMFB-M4AUO4NA-MMHMKMT"
        val skillId = "bc3842a3-ff9e-4b26-84b6-76b352063f16"
        val intentExample = "Wer ist dein Ersteller?"
        val apiEndpoint = "https://api.eu-de.assistant.watson.cloud.ibm.com"
        val assistantKey = "-oMRNlYmm5DZni5BDSEFCNclPiOfp9UpSHDw8-CeI66C"
        val assistantVersion = "2020-04-01"

        runBlocking {
            val response = trainingService.watsonRequest(chatbotKey, skillId, apiEndpoint, assistantKey, assistantVersion, intentExample)
            assertNotNull(response)
        }
    }

}