package com.echithub.asteroid

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.echithub.asteroid.R
import com.echithub.asteroid.databinding.FragmentDetailBinding
import com.echithub.asteroid.ui.viewmodel.DetailViewModel


class DetailFragment : Fragment() {

    private lateinit var mDetailViewModel: DetailViewModel
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDetailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        mDetailViewModel.fetch()

        mDetailViewModel.asteroidLiveData.observe(viewLifecycleOwner, Observer { asteroid ->
            asteroid?.let {
                Log.i("Asteroid Object :", asteroid.toString())
            }
        })
    }
}