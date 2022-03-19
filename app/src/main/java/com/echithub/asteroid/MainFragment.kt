package com.echithub.asteroid

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.echithub.asteroid.databinding.FragmentMainBinding
import com.echithub.asteroid.ui.adapters.AsteroidListAdapter
import com.echithub.asteroid.ui.viewmodel.MainViewModel
import com.echithub.asteroid.util.getNextSevenDaysFormattedDates
import com.echithub.asteroid.util.getProgressSpinner
import com.echithub.asteroid.util.loadImage
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {

    private lateinit var mAsteroidVideModel: MainViewModel
    private var _binding:FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val TAG = "Asteroid"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container,false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mAsteroidVideModel = ViewModelProvider(this).get(MainViewModel::class.java)

        mAsteroidVideModel.getPictureOfDayFromApi()
        mAsteroidVideModel.refreshData()

        val adapter = AsteroidListAdapter(arrayListOf())
        binding.asteroidRecycler.adapter = adapter
        binding.asteroidRecycler.layoutManager = LinearLayoutManager(requireContext())

        // Observe Live Data
        mAsteroidVideModel.readAllData.observe(viewLifecycleOwner, Observer { asteroids ->
            adapter.setData(asteroids)
        })

        mAsteroidVideModel.asteroidList.observe(viewLifecycleOwner, Observer { asteroids ->
            adapter.setData(asteroids)
        })

        mAsteroidVideModel.pictureOfDay.observe(viewLifecycleOwner, Observer { mediaOfDay ->
            binding.activityMainImageOfTheDay
                .loadImage(mediaOfDay.url, getProgressSpinner(binding.activityMainImageOfTheDay.context))

            binding.activityMainTxtTitle.text = mediaOfDay.title
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

        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = true
            mAsteroidVideModel.refreshData()
            binding.swipeRefreshLayout.isRefreshing = false
            Snackbar.make(binding.swipeRefreshLayout,"Database updated",Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater?.inflate(R.menu.filter_menu,menu)
        val listOfDates = getNextSevenDaysFormattedDates()
        menu.findItem(R.id.mn_filter_date_1).title = listOfDates[0]
        menu.findItem(R.id.mn_filter_date_2).title = listOfDates[1]
        menu.findItem(R.id.mn_filter_date_3).title = listOfDates[2]
        menu.findItem(R.id.mn_filter_date_4).title = listOfDates[3]
        menu.findItem(R.id.mn_filter_date_5).title = listOfDates[4]
        menu.findItem(R.id.mn_filter_date_6).title = listOfDates[5]
        menu.findItem(R.id.mn_filter_date_7).title = listOfDates[6]
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val listOfDates = getNextSevenDaysFormattedDates()
        return when(item.itemId){
            R.id.mn_filter_date_1 -> {
                mAsteroidVideModel.searchDate.value = listOfDates[0]
                mAsteroidVideModel.filterDateByDate()
                true
            }
            R.id.mn_filter_date_2 -> {
                mAsteroidVideModel.searchDate.value = listOfDates[1]
                mAsteroidVideModel.filterDateByDate()
                true
            }
            R.id.mn_filter_date_3 -> {
                mAsteroidVideModel.searchDate.value = listOfDates[2]
                mAsteroidVideModel.filterDateByDate()
                true
            }
            R.id.mn_filter_date_4 -> {
                mAsteroidVideModel.searchDate.value = listOfDates[3]
                mAsteroidVideModel.filterDateByDate()
                true
            }
            R.id.mn_filter_date_5 -> {
                mAsteroidVideModel.searchDate.value = listOfDates[4]
                mAsteroidVideModel.filterDateByDate()
                true
            }
            R.id.mn_filter_date_6 -> {
                mAsteroidVideModel.searchDate.value = listOfDates[5]
                mAsteroidVideModel.filterDateByDate()
                true
            }
            R.id.mn_filter_date_7 -> {
                mAsteroidVideModel.searchDate.value = listOfDates[6]
                mAsteroidVideModel.filterDateByDate()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

}