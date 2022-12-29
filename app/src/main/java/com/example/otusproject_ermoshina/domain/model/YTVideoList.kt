package com.example.otusproject_ermoshina.domain.model

data class YTVideoList(
    val id:Int,
    val idVideo: String,
    val title: String,
    val description: String,
    val image: String,
    val channelTitle:String,
    val videoPublishedAt:String,
    val channelId:String
)
