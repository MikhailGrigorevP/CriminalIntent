package com.mikhailgrigorev.criminalintent

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class CrimeActivity : AppCompatActivity() {

    val EXSTRACRIMEID: String = "com.mikhailgrigorev.criminalintent.crime_id"

    fun newIntent(packageContext: Context, crimeId:UUID): Intent {
        val intent = Intent(packageContext, CrimeActivity::class.java)
        intent.putExtra(EXSTRACRIMEID,crimeId)
        return intent
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragment)

        val fm = supportFragmentManager
        var fragment = fm.findFragmentById(R.id.fragment_container)
        if (fragment == null) {
            fragment = CrimeFragment()
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit()
        }
    }
}
