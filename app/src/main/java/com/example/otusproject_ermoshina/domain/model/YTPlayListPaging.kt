package com.example.otusproject_ermoshina.domain.model


data class YTPlayListPaging(
    val idChannel: String,
    val titleChannel: String,
    val nextToken: String,
    val listPlayList: List<YTPlayList>
)