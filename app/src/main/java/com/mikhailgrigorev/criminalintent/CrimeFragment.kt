package com.mikhailgrigorev.criminalintent

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import java.util.*


class CrimeFragment : Fragment() {
    private var mCrime: Crime? = null
    private var mTitleField: EditText? = null
    private var mDateButton: Button? = null
    private var mSolvedCheckbox: CheckBox? = null
    private var mReportButton: Button? = null
    private var mSuspectButton: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crimeId =
            arguments!!.getSerializable(ARG_CRIME_ID) as UUID?
        mCrime = CrimeLab[activity!!]!!.getCrime(crimeId!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.fragment_crime, container, false)
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
            val manager: FragmentManager? = fragmentManager
            val dialog = DatePickerFragment
                .newInstance(mCrime!!.date)
            dialog.setTargetFragment(this@CrimeFragment, REQUEST_DATE)
            if (manager != null) {
                dialog.show(manager, DIALOG_DATE)
            }
        }
        mSolvedCheckbox = v.findViewById<View>(R.id.crime_solved) as CheckBox
        mSolvedCheckbox!!.isChecked = mCrime!!.isSolved
        mSolvedCheckbox!!.setOnCheckedChangeListener { _, isChecked -> mCrime!!.isSolved = isChecked }
        mReportButton =
            v.findViewById<View>(R.id.crime_report) as Button
        mReportButton!!.setOnClickListener {
            var i = Intent(Intent.ACTION_SEND)
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_TEXT, crimeReport)
            i.putExtra(
                Intent.EXTRA_SUBJECT,
                getString(R.string.crime_report_subject)
            )
            i = Intent.createChooser(i, getString(R.string.send_report))
            startActivity(i)
        }
        val pickContact = Intent(
            Intent.ACTION_PICK,
            ContactsContract.Contacts.CONTENT_URI
        )
        mSuspectButton =
            v.findViewById<View>(R.id.crime_suspect) as Button
        mSuspectButton!!.setOnClickListener {
            startActivityForResult(
                pickContact,
                REQUEST_CONTACT
            )
        }
        if (mCrime!!.suspect != null) {
            mSuspectButton!!.text = mCrime!!.suspect
        }
        val packageManager = activity!!.packageManager
        if (packageManager.resolveActivity(
                pickContact,
                PackageManager.MATCH_DEFAULT_ONLY
            ) == null
        ) {
            mSuspectButton!!.isEnabled = false
        }
        return v
    }

    override fun onPause() {
        super.onPause()
        CrimeLab[activity!!]
            ?.updateCrime(mCrime!!)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_DATE) {
            val date = data
                ?.getSerializableExtra(DatePickerFragment.EXTRA_DATE) as Date
            mCrime!!.date = date
            updateDate()
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            val contactUri: Uri? = data.data
            // Specify which fields you want your query to return
            // values for.
            val queryFields = arrayOf(
                ContactsContract.Contacts.DISPLAY_NAME
            )
            // Perform your query - the contactUri is like a "where"
            // clause here
            val c: Cursor? = contactUri?.let {
                activity!!.contentResolver
                    .query(it, queryFields, null, null, null)
            }
            c.use { c ->
                // Double-check that you actually got results
                if (c != null) {
                    if (c.getCount() === 0) {
                        return
                    }
                }
                // Pull out the first column of the first row of data -
                // that is your suspect's name.
                c?.moveToFirst()
                val suspect: String = c!!.getString(0)
                mCrime!!.suspect = suspect
                mSuspectButton!!.text = suspect
            }
        }
    }

    private fun updateDate() {
        mDateButton!!.text = mCrime!!.date.toString()
    }

    private val crimeReport: String
        private get() {
            var solvedString: String? = null
            solvedString = if (mCrime!!.isSolved) {
                getString(R.string.crime_report_solved)
            } else {
                getString(R.string.crime_report_unsolved)
            }
            val dateFormat = "EEE, MMM dd"
            val dateString =
                DateFormat.format(dateFormat, mCrime!!.date).toString()
            var suspect = mCrime!!.suspect
            suspect = if (suspect == null) {
                getString(R.string.crime_report_no_suspect)
            } else {
                getString(R.string.crime_report_suspect, suspect)
            }
            return getString(
                R.string.crime_report,
                mCrime!!.title, dateString, solvedString, suspect
            )
        }

    companion object {
        private const val ARG_CRIME_ID = "crime_id"
        private const val DIALOG_DATE = "DialogDate"
        private const val REQUEST_DATE = 0
        private const val REQUEST_CONTACT = 1
        fun newInstance(crimeId: UUID?): CrimeFragment {
            val args = Bundle()
            args.putSerializable(ARG_CRIME_ID, crimeId)
            val fragment = CrimeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
