package com.mikhailgrigorev.criminalintent

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.util.*


class CrimeFragment : Fragment() {
    private var mCrime: Crime? = null
    private var mTitleField: EditText? = null
    private var mDateButton: Button? = null
    private var mSolvedCheckbox: CheckBox? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId =
            getArguments()?.getSerializable(ARG_CRIME_ID) as UUID
        mCrime = CrimeLab[activity]!!.getCrime(crimeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_crime, container, false)
        mTitleField = v.findViewById<View>(R.id.crime_title) as EditText
        mTitleField!!.setText(mCrime!!.title)
        mTitleField!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                mCrime!!.title = s.toString()
            }

            override fun afterTextChanged(s: Editable) {}
        })
        mDateButton = v.findViewById<View>(R.id.crime_date) as Button
        updateDate()
        mDateButton!!.setOnClickListener {
            val manager: FragmentManager? = getFragmentManager()
            val dialog = DatePickerFragment
                .newInstance(mCrime!!.date)
            dialog.setTargetFragment(this@CrimeFragment, REQUEST_DATE)
            if (manager != null) {
                dialog.show(manager, DIALOG_DATE)
            }
        }
        mSolvedCheckbox = v.findViewById<View>(R.id.crime_solved) as CheckBox
        mSolvedCheckbox!!.isChecked = mCrime!!.isSolved
        mSolvedCheckbox!!.setOnCheckedChangeListener { _, isChecked ->
            mCrime!!.isSolved = isChecked
        }
        return v
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_DATE) {
            val date = data
                ?.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
            mCrime!!.date = date
            updateDate()
        }
    }

    private fun updateDate() {
        mDateButton!!.text = mCrime!!.date.toString()
    }

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val DIALOG_DATE = "DialogDate"
        private const val REQUEST_DATE = 0
        fun newInstance(crimeId: UUID?): CrimeFragment {
            val args = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeId)
            val fragment = CrimeFragment()
            fragment.setArguments(args)
            return fragment
        }
    }
}
