package com.example.otusproject_ermoshina.domain.model

data class YTVideoListPaging(
    val idPlayList: String,
    val nextToken: String,
    val listVideoList: List<YTVideoList>
)
