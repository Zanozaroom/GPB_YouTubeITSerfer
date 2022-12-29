package com.example.otusproject_ermoshina.ui.fragment.videolist

import com.example.otusproject_ermoshina.domain.model.YTVideo

interface OnClickVideoList {
    fun onClickImage(idVideo: String)
    fun onClickIconOpenVideo(idVideo: String)
    fun onClickAddVideoToFavorite(idVideo: String)
}