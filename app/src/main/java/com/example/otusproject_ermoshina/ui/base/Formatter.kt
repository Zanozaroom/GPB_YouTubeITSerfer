package com.example.otusproject_ermoshina.ui.base

import java.text.SimpleDateFormat
import java.util.*

class Formatter {
    companion object{
        /**
         * Extension метод для парсинга входящей даты в нормальный формат
         */
        @JvmName("parse1")
        fun String.dataFormatter(): String{
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val date = dateFormat.parse(this)
            val formatter =
                SimpleDateFormat.getDateInstance() //If you need time just put specific format for time like 'HH:mm:ss'
            val dateStr = formatter.format(date!!)
            return dateStr
        }
    }

}