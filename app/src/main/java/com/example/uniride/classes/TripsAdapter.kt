package com.example.uniride.classes

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.uniride.R
import kotlinx.android.synthetic.main.upcoming_trip_list.view.*

class TripsAdapter(val context: Context, val  trips: List<Trip>) :  RecyclerView.Adapter<TripsAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.upcoming_trip_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val trip =trips[position]
        holder.setData(trip, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun setData(trip: Trip?, position: Int){
            itemView.tripList_date.text = trip!!.getDate()
        }

    }
}