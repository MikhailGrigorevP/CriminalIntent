package com.mikhailgrigorev.criminalintent.database

import android.database.Cursor
import android.database.CursorWrapper
import com.mikhailgrigorev.criminalintent.Crime
import com.mikhailgrigorev.criminalintent.database.CrimeDbSchema.CrimeTable.Cols
import java.util.*


class CrimeCursorWrapper(cursor: Cursor?) : CursorWrapper(cursor) {
    val crime: Crime
        get() {
            val uuidString = getString(getColumnIndex(Cols.UUID))
            val title = getString(getColumnIndex(Cols.TITLE))
            val date = getLong(getColumnIndex(Cols.DATE))
            val isSolved = getInt(getColumnIndex(Cols.SOLVED))
            val suspect = getString(getColumnIndex(Cols.SUSPECT))
            val crime = Crime(UUID.fromString(uuidString))
            crime.title = title
            crime.date = Date(date)
            crime.isSolved = isSolved != 0
            crime.suspect = suspect
            return crime
        }
}
