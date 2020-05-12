package com.mikhailgrigorev.criminalintent

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*


class CrimePagerActivity : AppCompatActivity(),
    CrimeFragment.Callbacks {
    private var mViewPager: ViewPager? = null
    private var mCrimes: List<Crime>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)
        val crimeId = intent
            .getSerializableExtra(EXTRA_CRIME_ID) as UUID
        mViewPager = findViewById<View>(R.id.crime_view_pager) as ViewPager
        mCrimes = CrimeLab[this]?.crimes
        val fragmentManager = supportFragmentManager
        mViewPager!!.adapter = object : FragmentStatePagerAdapter(fragmentManager) {
            override fun getItem(position: Int): Fragment {
                val crime = mCrimes!![position]
                return CrimeFragment.newInstance(crime.id)
            }

            override fun getCount(): Int {
                return mCrimes!!.size
            }
        }
        for (i in mCrimes!!.indices) {
            if (mCrimes!![i].id == crimeId) {
                mViewPager!!.currentItem = i
                break
            }
        }
    }

    override fun onCrimeUpdated(crime: Crime?) {}

    companion object {
        private const val EXTRA_CRIME_ID =
            "com.bignerdranch.android.criminalintent.crime_id"

        fun newIntent(packageContext: Context?, crimeId: UUID?): Intent {
            val intent = Intent(packageContext, CrimePagerActivity::class.java)
            intent.putExtra(EXTRA_CRIME_ID, crimeId)
            return intent
        }
    }
}
