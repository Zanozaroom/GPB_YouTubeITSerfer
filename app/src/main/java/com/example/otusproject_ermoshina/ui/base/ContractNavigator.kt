package com.example.otusproject_ermoshina.ui.base

interface ContractNavigator {
    fun startYTPlayListFragmentMainStack(idChannel: String)
    fun startSearchFragmentMainStack(question: String, title: String)
    fun startPageOfVideoFragmentMainStack(idVideo: String)
    fun startYTVideoListFragmentMainStack(idPlayList: String)
    fun startPageOfVideoFragmentUserStack(idVideo: String)
    fun startYTVideoListFragmentUserStack(idPlayList: String)
    fun startYTPlayListFragmentUserStack(idChannel: String)
    fun setActionBarNavigateBack()
    fun removeActionBarNavigateBack()
    fun setTitle(title: String?)

}