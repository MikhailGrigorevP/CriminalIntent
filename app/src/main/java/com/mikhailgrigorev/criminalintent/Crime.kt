package com.mikhailgrigorev.criminalintent

import java.util.*

class Crime {
    val id: UUID = UUID.randomUUID()
    var title: String? = null
    var date: Date = Date()
    var isSolved: Boolean = false

}
