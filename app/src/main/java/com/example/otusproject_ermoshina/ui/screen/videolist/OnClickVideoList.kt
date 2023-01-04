package com.example.otusproject_ermoshina.ui.screen.videolist


interface OnClickVideoList {
    fun onClickImage(idVideo: String)
    fun onClickIconOpenVideo(idVideo: String)
    fun onClickAddVideoToFavorite(idVideo: String)
    fun onClickOpenChannel(idChannel: String)
}