package com.example.otusproject_ermoshina.domain.repositories

sealed class NetworkResult<out T>
data class SuccessNetworkResult<out R>(val dataNetworkResult: R) : NetworkResult<R>()
object ErrorNetworkResult : NetworkResult<Nothing>()
object EmptyNetworkResult : NetworkResult<Nothing>()

