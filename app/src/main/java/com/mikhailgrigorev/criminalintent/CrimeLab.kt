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

    fun addCrime(c: Crime){
        mCrimes.add(c)
    }

    companion object {
        private var sCrimeLab: CrimeLab? = null
        operator fun get(context: FragmentActivity?): CrimeLab? {
            if (sCrimeLab == null) {
                sCrimeLab = context?.let { CrimeLab(it) }
            }
            return sCrimeLab
        }
    }

    init {
        mCrimes = ArrayList()
    }
}
