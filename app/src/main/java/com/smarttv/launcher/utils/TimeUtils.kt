package com.smarttv.launcher.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {

    private val timeFormat12 = SimpleDateFormat("hh:mm", Locale.getDefault())
    private val timeFormat24 = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateFormat = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault())
    private val amPmFormat = SimpleDateFormat("a", Locale.getDefault())

    fun formatTime(calendar: Calendar): String {
        return timeFormat12.format(calendar.time)
    }

    fun formatAmPm(calendar: Calendar): String {
        return amPmFormat.format(calendar.time)
    }

    fun formatDate(calendar: Calendar): String {
        return dateFormat.format(calendar.time)
    }
}
