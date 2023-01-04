package com.example.otusproject_ermoshina.domain.model

data class YTSearchPaging (
    val query: String,
    val nextToken: String,
    val listSearchVideo: List<YTSearch>
)