package com.example.otusproject_ermoshina.sources.helpers

open class BaseHelper {
    companion object{
        const val TOKEN_NULL = ""

        sealed class SafeToken {
            data class TokenChannel(var token: String = TOKEN_NULL)
            data class TokenPlayList(var token: String = TOKEN_NULL)
            data class TokenVideoListByPlayList(var token: String = TOKEN_NULL)
            data class TokenSearchMain(var token: String = TOKEN_NULL)
            data class TokenSearchResultVideoWithTitle(var token: String = TOKEN_NULL)
        }
    }
}