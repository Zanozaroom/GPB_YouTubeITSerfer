package com.example.otusproject_ermoshina.domain.model

data class YTMainFragmentData (
    val id: Int,
    val query:String,
    val title: String,
    val listResult:List<YTSearch>
)