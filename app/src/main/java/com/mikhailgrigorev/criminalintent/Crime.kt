package com.mikhailgrigorev.criminalintent

import java.util.*

class Crime @JvmOverloads constructor(val id: UUID = UUID.randomUUID()) {
    var title: String? = null
    var date: Date
    var isSolved = false
    var suspect: String? = null

    val photoFilename: String
        get() = "IMG_$id.jpg"

    init {
        date = Date()
    }
}
