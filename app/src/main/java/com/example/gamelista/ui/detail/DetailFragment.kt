package com.example.gamelista.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gamelista.R
import com.example.gamelista.adapter.GameListAdapter
import com.example.gamelista.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentDetailBinding? = null
    private lateinit var adapter: GameListAdapter
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initInfo()
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