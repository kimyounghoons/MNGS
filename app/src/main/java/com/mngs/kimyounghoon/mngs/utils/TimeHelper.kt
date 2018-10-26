package com.mngs.kimyounghoon.mngs.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeHelper {

    private const val LOCALE_TIME_FORMAT = "yyyy-MM-dd HH:mm"
    private const val UTC_TIME_ZONE = "UTC"

    fun getCurrentTime(): Long {
        return Calendar.getInstance().time.time
    }

    @JvmStatic
    fun getCurrentTimeLocale(time: Long): String {
        val sdf = SimpleDateFormat(LOCALE_TIME_FORMAT, Locale.getDefault())
        val date = Date(time)
        return sdf.format(date)
    }
}
