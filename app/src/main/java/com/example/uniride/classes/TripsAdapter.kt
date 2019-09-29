package com.example.uniride.classes

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.uniride.R
import com.example.uniride.activities.DriverInterface
import com.example.uniride.activities.EditTrip
import com.example.uniride.activities.UpcomingActivity
import com.example.uniride.showToast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.upcoming_trip_list.view.*

class TripsAdapter(val context: Context, val  trips: ArrayList<Trip>, val uIds: ArrayList<String>) :  RecyclerView.Adapter<TripsAdapter.MyViewHolder>(){

    val db: FirebaseFirestore = FirebaseFirestore.getInstance()

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
        val uId = uIds[position]
        holder.setData(trip, position, uId)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private var currentTrip: Trip? = null
        private var currentPosition: Int = 0
        private var currentUId: String? = null

        init {
            itemView.tripList_edit_button.setOnClickListener {

                val intent = Intent(context, EditTrip::class.java)
                intent.putExtra("Date", currentTrip?.getDate())
                intent.putExtra("Eta", currentTrip?.getArrival())
                intent.putExtra("Price", currentTrip?.getPrice())
                intent.putExtra("Car Detail", currentTrip?.getCar())
                intent.putExtra("NoPassengers", currentTrip?.getPassengerNo())
                intent.putExtra("uId", currentUId)
                context.startActivity(intent)
            }

            itemView.tripList_delete_Button.setOnClickListener {

                //Deletes the trip from database using Uid
                currentUId?.let { it1 ->
                    db.collection("trips").document(it1).delete()
                        .addOnSuccessListener { Log.d("Trip Delete", "DocumentSnapshot successfully deleted!") }
                        .addOnFailureListener { e -> Log.w("Trip Delete", "Error deleting document", e) }
                }

                //Deletes the current trip and Uid from the local array for refreshing the page
                trips.remove(currentTrip)
                uIds.remove(currentUId)
                notifyDataSetChanged()
                context.showToast("Trip Deleted!")
            }
        }

        //Sets the data in each text view
        fun setData(trip: Trip?, position: Int, uId: String){
            itemView.tripList_show_date.text = trip!!.getDate()
            itemView.tripList_show_car_details.text = trip.getCar()
            itemView.tripList_show_time.text = trip.getArrival()
            itemView.tripList_show_no_of_passenger.text = trip.getPassengerNo().toString()
            itemView.tripList_show_price.text = "$" + trip.getPrice().toString()
            itemView.tripList_show_route.text = trip.getRoute()
            itemView.tripList_show_driver_name.text = trip.getTripDriver()

            this.currentTrip = trip
            this.currentPosition = position
            this.currentUId = uId
        }
    }
}