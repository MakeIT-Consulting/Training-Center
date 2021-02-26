package de.makeit.ai.data

import com.google.gson.annotations.SerializedName

data class TrainingResponse(
    @SerializedName("training_code")
    val trainingCode: Int,
    @SerializedName("is_training")
    val isTraining: Boolean,
    val confidence: Double,
    val message: String
)
