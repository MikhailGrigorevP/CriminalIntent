package com.mikhailgrigorev.criminalintent.database

import android.database.Cursor
import android.database.CursorWrapper
import com.mikhailgrigorev.criminalintent.Crime
import com.mikhailgrigorev.criminalintent.database.CrimeDbSchema.CrimeTable
import java.util.*


class CrimeCursorWrapper(cursor: Cursor?) : CursorWrapper(cursor) {
    val crime: Crime
        get() {
            val uuidString = getString(getColumnIndex(CrimeTable.Cols.UUID))
            val title = getString(getColumnIndex(CrimeTable.Cols.TITLE))
            val date = getLong(getColumnIndex(CrimeTable.Cols.DATE))
            val isSolved = getInt(getColumnIndex(CrimeTable.Cols.SOLVED))
            val crime = Crime(UUID.fromString(uuidString))
            crime.title = title
            crime.date = Date(date)
            crime.isSolved = isSolved != 0
            return crime
        }
}