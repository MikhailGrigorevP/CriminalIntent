package com.mikhailgrigorev.criminalintent

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder


class CrimeListFragment: Fragment() {

    lateinit var mCrimeRecyclerView: RecyclerView
    var mAdapter: CrimeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_crime_list, container, false)
        mCrimeRecyclerView = view.findViewById<View>(R.id.crime_recycler_view) as RecyclerView
        mCrimeRecyclerView.layoutManager=LinearLayoutManager(activity)
        updateUI()
        return view
    }

    override fun onResume() {
        super.onResume()
        updateUI()
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
            mDateTextView.text = mCrime!!.date
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