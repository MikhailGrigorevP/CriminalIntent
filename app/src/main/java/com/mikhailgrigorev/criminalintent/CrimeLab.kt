package com.mikhailgrigorev.criminalintent

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.mikhailgrigorev.criminalintent.database.CrimeBaseHelper
import com.mikhailgrigorev.criminalintent.database.CrimeCursorWrapper
import com.mikhailgrigorev.criminalintent.database.CrimeDbSchema.CrimeTable
import com.mikhailgrigorev.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.DATE
import com.mikhailgrigorev.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.SOLVED
import com.mikhailgrigorev.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.SUSPECT
import com.mikhailgrigorev.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.TITLE
import com.mikhailgrigorev.criminalintent.database.CrimeDbSchema.CrimeTable.Cols.UUID
import java.util.*


class CrimeLab private constructor(context: Context) {
    private val mContext: Context = context.applicationContext
    private val mDatabase: SQLiteDatabase
    fun addCrime(c: Crime) {
        val values = getContentValues(c)
        mDatabase.insert(CrimeTable.NAME, null, values)
    }

    val crimes: List<Crime>
        get() {
            val crimes: MutableList<Crime> = ArrayList()
            val cursor: CrimeCursorWrapper = queryCrimes(null, null)
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
        val cursor: CrimeCursorWrapper = queryCrimes(
            CrimeTable.Cols.UUID + " = ?", arrayOf(id.toString())
        )
        return cursor.use { cursor ->
            if (cursor.getCount() === 0) {
                return null
            }
            cursor.moveToFirst()
            cursor.crime
        }
    }

    fun updateCrime(crime: Crime) {
        val uuidString = crime.id.toString()
        val values = getContentValues(crime)
        mDatabase.update(
            CrimeTable.NAME, values,
            CrimeTable.Cols.UUID + " = ?", arrayOf(uuidString)
        )
    }

    private fun queryCrimes(
        whereClause: String?,
        whereArgs: Array<String>?
    ): CrimeCursorWrapper {
        val cursor: Cursor = mDatabase.query(
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
        mDatabase = CrimeBaseHelper(mContext)
            .writableDatabase
    }
}
