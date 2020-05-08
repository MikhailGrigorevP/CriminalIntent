package com.mikhailgrigorev.criminalintent

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

class CrimeFragment : Fragment() {
    private var mCrime: Crime? = null
    private var mTitleField: EditText? = null
    private var mDateButton: Button? = null
    private var mSolvedCheckbox: CheckBox? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCrime = Crime()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.fragment_crime, container, false)
        mTitleField = v.findViewById<View>(R.id.crime_title) as EditText
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
        mDateButton!!.text = mCrime!!.date.toString()
        mDateButton!!.isEnabled = false

        mSolvedCheckbox = v.findViewById<View>(R.id.crime_solved) as CheckBox
        mSolvedCheckbox!!.setOnCheckedChangeListener { buttonView, isChecked ->
            mCrime!!.isSolved = isChecked
        }
        return v
    }
}