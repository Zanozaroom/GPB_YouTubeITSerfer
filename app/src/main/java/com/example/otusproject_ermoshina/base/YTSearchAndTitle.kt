package com.example.otusproject_ermoshina.base

data class YTSearchAndTitle (
    val id: Int,
    val query:String,
    val title: String,
    val listResult:List<YTSearch>
)