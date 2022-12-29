package com.example.otusproject_ermoshina.domain.model

data class YTVideo(
    val idVideo: String,
    val image: String,
    val title: String,
    val publishedAt:String,
    val description: String,
    val channelTitle: String,
    val channelId: String,
    val viewCount: Int,
    val likeCount: Int
)

