package com.example.otusproject_ermoshina.domain

open class AppExceptionsBase(
    sayMe: String, private val sayAbout: String = "Ошибка загрузки данных $sayMe"
):Exception(){
    fun sayException() = sayAbout
}

class DataBaseLoadException(sayMe: String): AppExceptionsBase(sayMe)
class NetworkLoadException(sayMe: String): AppExceptionsBase(sayMe)
class StrangeException(sayMe: String): AppExceptionsBase(sayMe)
