package com.example.otusproject_ermoshina.sources.retrofit.models

import com.google.gson.annotations.SerializedName
data class Error(
    @SerializedName("code")
    var code: Int,
    @SerializedName("message")
    var message: String,
    @SerializedName("status")
    var status: String
)




