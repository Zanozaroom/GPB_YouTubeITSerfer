package com.example.otusproject_ermoshina.sources


import javax.inject.Inject

class LoadingHelper (
    private val network: RepositoryNetwork,
    private val dataBase: RepositoryDataBase
) {


/*



    suspend fun getLoadMoreResultSearch(query: String): List<YTSearch>? {
        if (tokenSearchMain.token.isBlank()) return null
        else return getResultSearch(query, tokenSearchMain.token)
    }

    suspend fun getResultSearch(query: String, token: String = TOKEN_NULL): List<YTSearch> {
        val result = network.getResultSearch(query, token)
        tokenSearchMain.token = result.nextPageToken ?: TOKEN_NULL
        return result.toYTSearch()
    }

*
     * Вызываем для каждого поискового запроса, чтобы получить все видео по запросу(с пагинацией)


    suspend fun getQueryApp(): List<YTSearchAndTitle> {
        _listFromDataBaseQuery.addAll(getQueryAppRoom())
        _listFromDataBaseQuery.forEach {
            getNetworkDataQuery(it.query, TOKEN_NULL,  emptyList())
            _listQueryAndTitle.add(YTSearchAndTitle(
                id=_listQuery.hashCode(),
                title = it.titleQuery,
                listResult = _listQuery
            ))
        }
        return _listQueryAndTitle
    }


*
     * Вызываем для получение вопросов из базы данных, разбитых по темам


    private suspend fun getQueryAndThemes():List<RoomQueryAndTheme> {
        try {
            return dataBase.loadQueryAndTheme()
        } catch (e: Exception) {
            throw DataBaseLoadException(e.toString())
        }
    }
    suspend fun getSearchAndTheme(){
        val listRoomQueryAndTheme = getQueryAndThemes()
        listRoomQueryAndTheme.forEach {
            val idTheme = it.theme.id
            val theme = it.theme.theme
           it.queryList.forEach {

           }
          // возврат  :List<YTSearchAndTheme>
        }

    }*/




 /*   companion object {
        private const val TOKEN_NULL = ""

        sealed class SafeToken {
            data class TokenChannel(var token: String = TOKEN_NULL)
            data class TokenPlayList(var token: String = TOKEN_NULL)
            data class TokenVideoListByPlayList(var token: String = TOKEN_NULL)
            data class TokenSearchMain(var token: String = TOKEN_NULL)
        }
    }*/

}
