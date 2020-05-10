package com.mikhailgrigorev.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import androidx.fragment.app.Fragment
import java.util.*


class CrimeFragment : Fragment() {
    private var ARG_CRIME_ID: String = "crime_id"
    private var DIALOG_DATE = "DialogDate"
    private var mCrime: Crime? = null
    private var mTitleField: EditText? = null
    private var mDateButton: Button? = null
    private var mSolvedCheckbox: CheckBox? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        mCrime = CrimeLab.get(activity!!)?.getCrime(crimeID)
    }

    fun newInstance(crimeId: UUID): CrimeFragment{
        val args = Bundle()
        args.putSerializable(ARG_CRIME_ID, crimeId)

        val fragment = CrimeFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v: View = inflater.inflate(R.layout.fragment_crime, container, false)
        mTitleField = v.findViewById<View>(R.id.crime_title) as EditText
        mTitleField!!.setText(mCrime?.title)
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
        mDateButton!!.text = mCrime?.date
        mDateButton!!.setOnClickListener (View.OnClickListener(){
            val manager = fragmentManager
            val dialog = DatePickerFragment()
            if (manager != null) {
                dialog.show(manager, DIALOG_DATE)
            }

        })

        mSolvedCheckbox = v.findViewById<View>(R.id.crime_solved) as CheckBox
        mSolvedCheckbox!!.isChecked = mCrime!!.isSolved
        mSolvedCheckbox!!.setOnCheckedChangeListener { buttonView, isChecked -> mCrime!!.isSolved = isChecked }
        return v
    }

}