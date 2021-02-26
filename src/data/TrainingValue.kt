package de.makeit.ai.data

import com.google.gson.annotations.SerializedName
import de.makeit.klib.mole.data.MoleBasicClass

data class TrainingValue(
    @SerializedName("chatbot_key")
    val chatbotKey: String,
    @SerializedName("skill_id")
    val skillId: String,
    @SerializedName("api_endpoint")
    val apiEndpoint: String,
    @SerializedName("assistant_key")
    val assistantKey: String,
    @SerializedName("assistant_version")
    val assistantVersion: String,
    @SerializedName("intent_example")
    val intentExample: String
) : MoleBasicClass()
