package com.example.otusproject_ermoshina.base

open class AppExceptionsBase(
    sayMe: String, private val sayAbout: String = "Ошибка загрузки из базы данных $sayMe"
):Throwable(){
    fun sayException() = sayAbout
}

class RoomLoadException(sayMe: String):AppExceptionsBase(sayMe)

class RetrofitAbsoluteLoadException(sayMe: String):AppExceptionsBase(sayMe)
class RetrofitBodyIsSuccessfulException(sayMe: String):AppExceptionsBase(sayMe)
