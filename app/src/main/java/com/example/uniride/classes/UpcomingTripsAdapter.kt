package com.example.uniride.classes

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.uniride.R
import com.example.uniride.activities.EditTrip
import com.example.uniride.activities.Messenger
import com.example.uniride.showToast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.upcoming_trip_list.view.*

class UpcomingTripsAdapter(val context: Context, val  trips: ArrayList<Trip>, val uIds: ArrayList<String>) :  RecyclerView.Adapter<UpcomingTripsAdapter.MyViewHolder>(){

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

                //Building dialog message
                val alertDialogForDelete = AlertDialog.Builder(context)
                alertDialogForDelete.setTitle("Delete Trip")
                alertDialogForDelete.setMessage("Are You Sure You Want To Delete This Trip?")

                //Add positive button to alert dialog
                alertDialogForDelete.setPositiveButton("YES"){dialog, id ->
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

                //Add negative button to alert dialog
                alertDialogForDelete.setNegativeButton("NO"){dialog, id ->
                    dialog.dismiss()
                }
                alertDialogForDelete.show()
            }

            itemView.upcoming_message_button.setOnClickListener {

                var passengers = currentTrip?.getPassengerList()
                Log.d("PassengerList", passengers.toString())
                val p = PopupMenu(context, itemView.upcoming_message_button)

                if (passengers != null) {
                    var i = 0
                    for (item in passengers) {
                        Log.d("Loop", "Inside Loop")
                        val docRef = db.collection("users").whereEqualTo("address", item)
                            .get().addOnSuccessListener { document ->
                                if (document != null) {
                                    Log.d("Document", "not null")
                                    p.menu.add(document.elementAt(0).getString("email"))
                                }
                            }
                        i++
                    }
                }
                p.menuInflater.inflate(R.menu.message_passenger_menu, p.menu)
                p.setOnMenuItemClickListener(object: PopupMenu.OnMenuItemClickListener {
                    override fun onMenuItemClick(item: MenuItem): kotlin.Boolean {
                        lateinit var fbUserID: String
                        Log.d("item.title", item.title.toString())
                        if (item.title.equals("Passengers:")) {
                            Toast.makeText(context, "You clicked Passengers:... Click a name.", Toast.LENGTH_SHORT).show()
                            Log.d("if", "clicked passengers")
                            return false
                        }
                        else
                        {
                            Log.d("if passenger", item.title.toString())
                            var docRef = db.collection("users").document(item.title.toString())
                            docRef.get().addOnSuccessListener { document ->
                                    if (document != null) {
                                        fbUserID = document.getString("fbUserID").toString()
                                        Log.d("Success, UpcomingTrip", fbUserID)
                                        val intent = Intent(context , Messenger::class.java)
                                        intent.putExtra("userID", fbUserID)
                                        context.startActivity(intent)
                                    } else
                                        Log.d("Fail, UpcomingTrip", "Document didn't retrieve")
                            }
                            return false
                        }
                    }
                })
                p.show()

                /*
                val userID = "royalty37"
                val intent = Intent(mContext, Messenger::class.java)
                intent.putExtra("userID", userID)
                mContext.startActivity(intent)
                */
            }
        }

        //Sets the data in each text view
        fun setData(trip: Trip?, position: Int, uId: String){
            itemView.tripList_show_date.text = trip!!.getDate()
            itemView.tripList_show_car_details.text = trip.getCar()
            itemView.tripList_show_time.text = trip.getArrival()
            itemView.tripList_show_no_of_passenger.text = trip.getPassengerNo().toString()
            itemView.tripList_show_price.text = "$" + trip.getPrice().toString()
            itemView.tripList_show_route.text = trip.getRouteOfTrip()
            itemView.tripList_show_driver_name.text = trip.getTripDriver()

            if(trip.getPassengerList() != null){
                for (item in trip.getPassengerList()!!){
                    itemView.tripList_show_passengerList.append(item)
                    itemView.tripList_show_passengerList.append("\n")
                }
            }

            this.currentTrip = trip
            this.currentPosition = position
            this.currentUId = uId
        }
    }
}