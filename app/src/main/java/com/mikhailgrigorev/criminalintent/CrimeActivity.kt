package com.mikhailgrigorev.criminalintent

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import java.util.*

class CrimeActivity : SingleFragmentActivity() {

    private val EXSTRACRIMEID: String = "com.mikhailgrigorev.criminalintent.crime_id"

    fun newIntent(packageContext: Context, crimeId:UUID): Intent {
        val intent = Intent(packageContext, CrimeActivity::class.java)
        intent.putExtra(EXSTRACRIMEID,crimeId)
        return intent
    }

    override fun createFragment(): Fragment{
        val crimeId: UUID = intent.getSerializableExtra(EXSTRACRIMEID) as UUID
        return CrimeFragment().newInstance(crimeId)

    }
}
