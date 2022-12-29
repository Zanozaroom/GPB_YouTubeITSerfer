package com.example.otusproject_ermoshina.domain.model

import com.example.otusproject_ermoshina.domain.model.YTSearch

data class YTSearchAndTitle (
    val id: Int,
    val query:String,
    val title: String,
    val listResult:List<YTSearch>
)