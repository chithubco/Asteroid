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
import com.echithub.asteroid.ui.viewmodel.DetailViewModel
import com.echithub.asteroid.util.getProgressSpinner
import com.echithub.asteroid.util.loadImage


class DetailFragment : Fragment() {

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


        mDetailViewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        mDetailViewModel.refresh(args.asteroidId)


        mDetailViewModel.asteroidLiveData.observe(viewLifecycleOwner, Observer { asteroid ->
            Log.i("Asteroid Id:","Got here")
            asteroid?.let {

                binding.activityDetailImage.loadImage(asteroid.url,
                    getProgressSpinner(binding.activityDetailImage.context)
                )
                binding.absoluteMagnitude.text = asteroid.absoluteMagnitude.toString()
                binding.closeApproachDate.text = asteroid.closeApproachDate
                binding.distanceFromEarth.text = asteroid.distanceFromEarth.toString()
                binding.estimatedDiameter.text = asteroid.estimatedDiameter.toString()
                binding.relativeVelocity.text = asteroid.relativeVelocity.toString()

            }
        })

        binding.activityDetailImage.setOnClickListener {
            openDialog("This is a good thing","Asteroid")

        }


    }

    private fun openDialog(message: String,title:String){
        // Setup Dialog
        val builder: AlertDialog.Builder? = activity?.let {
            AlertDialog.Builder(it)
        }
        builder?.setMessage(message)?.setTitle(title)
        val dialog: AlertDialog? = builder?.create()
        dialog?.show()
    }


//    fun onCreateDialog(savedInstanceState: Bundle): Dialog {
//        return activity?.let {
//            // Use the Builder class for convenient dialog construction
//            val builder = AlertDialog.Builder(it)
//            builder.setMessage("Start")
//                .setPositiveButton("Fire",
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // START THE GAME!
//                    })
//                .setNegativeButton("Cancel",
//                    DialogInterface.OnClickListener { dialog, id ->
//                        // User cancelled the dialog
//                    })
//            // Create the AlertDialog object and return it
//            builder.create()
//        } ?: throw IllegalStateException("Activity cannot be null")
//    }
}