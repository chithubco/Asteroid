package com.echithub.asteroid

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.echithub.asteroid.databinding.FragmentDetailBinding
import com.echithub.asteroid.ui.listener.AsteroidClickLister
import com.echithub.asteroid.ui.viewmodel.DetailViewModel
import com.echithub.asteroid.util.getProgressSpinner
import com.echithub.asteroid.util.loadImage


class DetailFragment : Fragment(),AsteroidClickLister{

    private lateinit var mDetailViewModel: DetailViewModel
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args: DetailFragmentArgs by navArgs()

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

        binding.imageClickLister = this

        mDetailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        mDetailViewModel.refresh(args.asteroidId)
        mDetailViewModel.asteroidLiveData.observe(viewLifecycleOwner, Observer { asteroid ->
            asteroid?.let {
                binding.asteroid = asteroid
            }
        })
    }

    private fun openDialog(message: String,title:String){
        // Setup Dialog
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }
        builder?.setMessage(message)?.setTitle(title)?.setPositiveButton("ACCEPT",null)
        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }

    override fun onAsteroidImageClicked(v: View) {
        openDialog(getString(R.string.astronomica_unit_explanation),"ASTRONOMIC UNITS")
    }

}