package com.mikhailgrigorev.criminalintent

import java.text.DateFormat
import java.util.*

class Crime {
    val id: UUID = UUID.randomUUID()
    var title: String? = null
    var date: String = DateFormat.getDateInstance().format(Date())
    var isSolved: Boolean = false

}
