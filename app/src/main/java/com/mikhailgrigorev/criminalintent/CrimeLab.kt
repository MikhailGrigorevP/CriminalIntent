package com.mikhailgrigorev.criminalintent

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.mikhailgrigorev.criminalintent.database.CrimeBaseHelper
import com.mikhailgrigorev.criminalintent.database.CrimeCursorWrapper
import com.mikhailgrigorev.criminalintent.database.CrimeDbSchema.CrimeTable
import com.mikhailgrigorev.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.DATE
import com.mikhailgrigorev.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.SOLVED
import com.mikhailgrigorev.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.SUSPECT
import com.mikhailgrigorev.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.TITLE
import com.mikhailgrigorev.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.UUID
import java.io.File
import java.util.*


class CrimeLab private constructor(context: Context) {
    private val mContext: Context
    private val mDatabase: SQLiteDatabase
    fun addCrime(c: Crime) {
        val values = getContentValues(c)
        mDatabase.insert(CrimeTable.NAME, null, values)
    }

    val crimes: List<Crime>
        get() {
            val crimes: MutableList<Crime> = ArrayList()
            val cursor = queryCrimes(null, null)
            try {
                cursor.moveToFirst()
                while (!cursor.isAfterLast) {
                    crimes.add(cursor.crime)
                    cursor.moveToNext()
                }
            } finally {
                cursor.close()
            }
            return crimes
        }

    fun getCrime(id: UUID): Crime? {
        val cursor = queryCrimes(
            "$UUID = ?", arrayOf(id.toString())
        )
        return try {
            if (cursor.count == 0) {
                return null
            }
            cursor.moveToFirst()
            cursor.crime
        } finally {
            cursor.close()
        }
    }

    fun getPhotoFile(crime: Crime): File {
        val filesDir: File = mContext.filesDir
        return File(filesDir, crime.photoFilename)
    }

    fun updateCrime(crime: Crime) {
        val uuidString = crime.id.toString()
        val values = getContentValues(crime)
        mDatabase.update(
            CrimeTable.NAME, values,
            "$UUID = ?", arrayOf(uuidString)
        )
    }

    private fun queryCrimes(
        whereClause: String?,
        whereArgs: Array<String>?
    ): CrimeCursorWrapper {
        val cursor = mDatabase.query(
            CrimeTable.NAME,
            null,  // Columns - null selects all columns
            whereClause,
            whereArgs,
            null,  // groupBy
            null,  // having
            null // orderBy
        )
        return CrimeCursorWrapper(cursor)
    }

    companion object {
        private var sCrimeLab: CrimeLab? = null
        operator fun get(context: Context): CrimeLab? {
            if (sCrimeLab == null) {
                sCrimeLab = CrimeLab(context)
            }
            return sCrimeLab
        }

        private fun getContentValues(crime: Crime): ContentValues {
            val values = ContentValues()
            values.put(UUID, crime.id.toString())
            values.put(TITLE, crime.title)
            values.put(DATE, crime.date.time)
            values.put(SOLVED, if (crime.isSolved) 1 else 0)
            values.put(SUSPECT, crime.suspect)
            return values
        }
    }

    init {
        mContext = context.applicationContext
        mDatabase = CrimeBaseHelper(mContext)
            .writableDatabase
    }
}
