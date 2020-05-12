package com.mikhailgrigorev.criminalintent

import android.view.View
import androidx.fragment.app.Fragment

class CrimeListActivity : SingleFragmentActivity(),
    CrimeListFragment.Callbacks {
    override fun createFragment(): Fragment? {
        return CrimeListFragment()
    }

    override fun getLayoutResId(): Int {
        return R.layout.activity_masterdetail
    }

    override fun onCrimeSelected(crime: Crime?) {
        if (findViewById<View?>(R.id.detail_fragment_container) == null) {
            val intent = CrimePagerActivity.newIntent(this, crime!!.id)
            startActivity(intent)
        } else {
            val newDetail: Fragment = CrimeFragment.newInstance(crime!!.id)
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, newDetail)
                .commit()
        }
    }

    fun onCrimeUpdated(crime: Crime?) {
        val listFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_container) as CrimeListFragment?
        listFragment!!.updateUI()
    }
}
