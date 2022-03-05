package com.echithub.asteroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.echithub.asteroid.databinding.FragmentMainBinding
import com.echithub.asteroid.ui.adapters.AsteroidListAdapter
import com.echithub.asteroid.ui.viewmodel.MainViewModel

class MainFragment : Fragment() {

    private lateinit var mAsteroidVideModel: MainViewModel
    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAsteroidVideModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mAsteroidVideModel.refresh()

        val adapter = AsteroidListAdapter(arrayListOf())
        binding.asteroidRecycler.adapter = adapter
        binding.asteroidRecycler.layoutManager = LinearLayoutManager(requireContext())

        // Observe Live Data
//        mAsteroidVideModel.readAllData.observe(viewLifecycleOwner, Observer { asteroids ->
//            Log.i("Asteroid List",asteroids.toString())
//            adapter.setData(asteroids)
//        })

        mAsteroidVideModel.asteroids.observe(viewLifecycleOwner, Observer { asteroids ->
            Log.i("Asteroid List",asteroids.toString())
            Log.i("Asteroid Size",asteroids.size.toString())
            adapter.setData(asteroids)
        })

        mAsteroidVideModel.pictureOfDay.observe(viewLifecycleOwner, Observer { mediaOfDay ->

        })

        mAsteroidVideModel.hasError.observe(viewLifecycleOwner, Observer { error ->
            error?.let {
                Log.i("Asteroid : State",error.toString())
            }
        })

        mAsteroidVideModel.isLoading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {
                Log.i("Asteroid : State",loading.toString())
            }
        })
    }

}