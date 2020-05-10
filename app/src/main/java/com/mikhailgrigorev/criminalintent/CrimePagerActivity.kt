package com.mikhailgrigorev.criminalintent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import java.util.*


class CrimePagerActivity : AppCompatActivity() {
    private var mViewPager: ViewPager? = null
    private var mCrimes: List<Crime>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crime_pager)
        val crimeId = intent
            .getSerializableExtra(EXTRA_CRIME_ID) as UUID
        mViewPager = findViewById<View>(R.id.crime_view_pager) as ViewPager
        mCrimes = CrimeLab[this]!!.crimes
        val fragmentManager: FragmentManager = supportFragmentManager
        mViewPager!!.adapter = object : FragmentPagerAdapter(fragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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
