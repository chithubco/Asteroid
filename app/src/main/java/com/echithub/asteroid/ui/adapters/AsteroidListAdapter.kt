package com.echithub.asteroid.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.echithub.asteroid.MainFragmentDirections
import com.echithub.asteroid.R
import com.echithub.asteroid.data.model.Asteroid

class AsteroidListAdapter(val asteroidList: ArrayList<Asteroid>): RecyclerView.Adapter<AsteroidListAdapter.MyViewHolder>() {

//    private var asteroidList = emptyList<Asteroid>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.asteroid_row, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var currentItem = asteroidList[position]
        holder.itemView.findViewById<TextView>(R.id.tv_asteroid_id).text = currentItem.id.toString()
        holder.itemView.findViewById<TextView>(R.id.tv_asteroid_date).text = currentItem.codename

        // Set on click listener
//        holder.itemView.findViewById<ConstraintLayout>(R.id.asteroidRowLayout).setOnClickListener{
//
//        }
        holder.itemView.setOnClickListener {
            Navigation.findNavController(it).navigate(MainFragmentDirections.actionMainFragmentToDetailFragment())
        }
    }

    override fun getItemCount(): Int {
        Log.i("Asteroid Item Count : ", asteroidList.size.toString())
        return asteroidList.size
    }

    fun setData(asteroids: List<Asteroid>){
        asteroidList.clear()
        asteroidList.addAll(asteroids)
        notifyDataSetChanged()
    }
}