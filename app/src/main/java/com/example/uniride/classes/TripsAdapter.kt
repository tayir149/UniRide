package com.example.uniride.classes

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.uniride.R
import com.example.uniride.activities.DriverInterface
import kotlinx.android.synthetic.main.upcoming_trip_list.view.*

class TripsAdapter(val context: Context, val  trips: List<Trip>) :  RecyclerView.Adapter<TripsAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.upcoming_trip_list, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return trips.size
    }

    //Bind the data to the card view
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val trip =trips[position]
        holder.setData(trip, position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var currentTrip: Trip? = null
        var currentPosition: Int = 0

        init {

            itemView.tripList_edit_button.setOnClickListener {
                val intent = Intent(context, DriverInterface::class.java)
                context.startActivity(intent)
            }

            itemView.tripList_delete_Button.setOnClickListener {
                val intent = Intent(context, DriverInterface::class.java)
                context.startActivity(intent)
            }
        }

        //Sets the data in each text view
        fun setData(trip: Trip?, position: Int){
            itemView.tripList_show_date.text = trip!!.getDate()
            itemView.tripList_show_car_details.text = trip.getCar()
            itemView.tripList_show_time.text = trip.getArrival()
            itemView.tripList_show_no_of_passenger.text = trip.getPassengerNo().toString()
            itemView.tripList_show_price.text = "$" + trip.getPrice().toString()
            itemView.tripList_show_route.text = trip.getRoute()
            itemView.tripList_show_driver_name.text = trip.getTripDriver()

            this.currentTrip = trip
            this.currentPosition = position
        }
    }
}