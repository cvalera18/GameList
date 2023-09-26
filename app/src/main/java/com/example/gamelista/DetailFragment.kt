package com.example.gamelista

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gamelista.adapter.GameListAdapter
import com.example.gamelista.databinding.FragmentDetailBinding
import com.example.gamelista.databinding.FragmentFavBinding

class DetailFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentDetailBinding? = null
    private lateinit var adapter: GameListAdapter
    private val llmanager = LinearLayoutManager(activity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInfo()
//        val ID = arguments?.getString("ID")
//        Toast.makeText(requireContext(), "ID = $ID", Toast.LENGTH_LONG).show()
        binding.ivBackArrow.setOnClickListener {
            findNavController().navigateUp()
        }



    }
    private fun initInfo() {
        val NAME = arguments?.getString("NAME")
        val PLAT = arguments?.getString("PLAT")
        val STATUS = arguments?.getString("STATUS")
        val PIC = arguments?.getString("PIC")
        val SINOP = arguments?.getString("SINOP")
        val DEV = arguments?.getString("DEV")
        val FAV = arguments?.getBoolean("FAV")
        if (FAV == true) {
            binding.ivStarDetail.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.ivStarDetail.context,
                    R.drawable.baseline_star_24
                )
            )
        } else {
            binding.ivStarDetail.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.ivStarDetail.context,
                    R.drawable.baseline_star_outline_24
                )
            )
        }
        binding.tvGameName.text = NAME
        binding.tvPlatformSpec.text = PLAT
        binding.tvStatusSpec.text = STATUS
        Glide.with(binding.ivGameDetail.context).load(PIC).centerCrop().into(binding.ivGameDetail)
        binding.tvSinopsisSpec.text = SINOP
        binding.tvDevSpec.text = DEV
    }
}