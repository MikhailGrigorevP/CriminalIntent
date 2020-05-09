package com.mikhailgrigorev.criminalintent

import android.content.Context
import androidx.fragment.app.FragmentActivity
import java.util.*


class CrimeLab private constructor(context: Context) {
    private val mCrimes: MutableList<Crime>
    val crimes: List<Crime>
        get() = mCrimes

    fun getCrime(id: UUID): Crime? {
        for (crime in mCrimes) {
            if (crime.id == id) {
                return crime
            }
        }
        return null
    }

    companion object {
        fun get(context: Context): CrimeLab? {
            return CrimeLab(context)
        }
    }

    init {
        mCrimes = ArrayList()
        for (i in 0..99) {
            val crime = Crime()
            crime.title = "Crime #$i"
            crime.isSolved = i % 2 == 0
            mCrimes.add(crime)
        }
    }
}
