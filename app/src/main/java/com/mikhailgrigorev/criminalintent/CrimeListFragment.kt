package com.mikhailgrigorev.criminalintent

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class CrimeListFragment: Fragment() {

    private val SAVED_SUBTITLE_VISIBLE = "subtitle"
    lateinit var mCrimeRecyclerView: RecyclerView
    var mAdapter: CrimeAdapter? = null
    var mSubtitleVisible = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_crime_list, container, false)
        mCrimeRecyclerView = view.findViewById<View>(R.id.crime_recycler_view) as RecyclerView
        mCrimeRecyclerView.layoutManager=LinearLayoutManager(activity)
        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        updateUI()
        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_crime -> {
                val crime = Crime()
                CrimeLab[activity]!!.addCrime(crime)
                val intent = CrimePagerActivity
                    .newIntent(activity, crime.id)
                startActivity(intent)
                true
            }
            R.id.show_subtitle -> {
                mSubtitleVisible = !mSubtitleVisible
                activity!!.invalidateOptionsMenu()
                updateSubtitle()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun updateSubtitle() {
        val crimeLab = CrimeLab[activity]
        val crimeCount = crimeLab!!.crimes.size
        var subtitle: String? = getString(R.string.subtitle_format, crimeCount)
        if (!mSubtitleVisible) {
            subtitle = null
        }
        val activity = activity as AppCompatActivity?
        activity!!.supportActionBar!!.setSubtitle(subtitle)
    }


    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_crime_list, menu)

        val subtitleItem = menu.findItem(R.id.show_subtitle)
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle)
        } else {
            subtitleItem.setTitle(R.string.show_subtitle)
        }
    }

    private fun updateUI() {
        val crimeLab = CrimeLab[activity!!]
        val crimes = crimeLab!!.crimes
        if (mAdapter == null) {
            mAdapter = CrimeAdapter(crimes)
            mCrimeRecyclerView.adapter = mAdapter
        } else {
            mAdapter!!.notifyDataSetChanged()
        }

        updateSubtitle()
    }

    class CrimeHolder(inflater: LayoutInflater, parent: ViewGroup?) :
        ViewHolder(inflater.inflate(R.layout.list_item_crime, parent, false)),
        View.OnClickListener {
        private var mCrime: Crime? = null
        private val mTitleTextView: TextView
        private val mDateTextView: TextView
        private val mSolvedImageView: ImageView
        fun bind(crime: Crime?) {
            mCrime = crime
            mTitleTextView.text = mCrime!!.title
            mDateTextView.text = mCrime!!.date.toString()
            if (crime != null) {
                mSolvedImageView.visibility = if (crime.isSolved) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }

        override fun onClick(view: View) {
            val intent: Intent = CrimePagerActivity.newIntent(view.context, mCrime!!.id)
            view.context.startActivity(intent)
        }

        init {
            itemView.setOnClickListener(this)
            mTitleTextView = itemView.findViewById<View>(R.id.crime_title) as TextView
            mDateTextView = itemView.findViewById<View>(R.id.crime_date) as TextView
            mSolvedImageView = itemView.findViewById<View>(R.id.crime_solved) as ImageView
        }
    }

    class CrimeAdapter(crimes: List<Crime>?): RecyclerView.Adapter<CrimeHolder>() {
        private var mCrimes = crimes
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return CrimeHolder(layoutInflater, parent)
        }

        override fun getItemCount(): Int {
            return mCrimes?.size!!
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime: Crime? = mCrimes?.get(position)
            if (crime != null) {
                holder.bind(crime)
            }
        }

    }
}