package com.app.usecase

import java.util.*

class CalendarWrapper {
    open fun getInstance(): Calendar {
        return Calendar.getInstance()
    }
}