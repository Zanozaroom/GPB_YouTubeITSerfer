package com.example.otusproject_ermoshina.base

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Video(
    val id: String,
    val title: String,
    val description: String?
) : Parcelable