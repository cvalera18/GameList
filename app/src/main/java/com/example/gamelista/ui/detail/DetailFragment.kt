package com.example.gamelista.ui.detail

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gamelista.R
import com.example.gamelista.adapter.GameListAdapter
import com.example.gamelista.data.Repository.onFavItem
import com.example.gamelista.data.Repository.onListedItem
import com.example.gamelista.databinding.FragmentDetailBinding
import com.example.gamelista.model.Game

class DetailFragment : Fragment() {

    private val binding get() = _binding!!
    private var _binding: FragmentDetailBinding? = null

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
        val PLAT2 = arguments?.getString("PLAT2")
        val PLAT3 = arguments?.getString("PLAT3")
        val PLAT4 = arguments?.getString("PLAT4")
        val PLAT5 = arguments?.getString("PLAT5")
        val STATUS = arguments?.getString("STATUS")
        val PIC = arguments?.getString("PIC")
        val SINOP = arguments?.getString("SINOP")
        val DEV = arguments?.getString("DEV")
        val FAV = arguments?.getBoolean("FAV")
        val DATE = arguments?.getString("DATE")
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
        Glide.with(binding.ivGameDetail.context).load(PLAT).fitCenter().into(binding.ivPlat1)
        Glide.with(binding.ivGameDetail.context).load(PLAT2).fitCenter().into(binding.ivPlat2)
        Glide.with(binding.ivGameDetail.context).load(PLAT3).fitCenter().into(binding.ivPlat3)
        Glide.with(binding.ivGameDetail.context).load(PLAT4).fitCenter().into(binding.ivPlat4)
        Glide.with(binding.ivGameDetail.context).load(PLAT5).fitCenter().into(binding.ivPlat5)
        binding.tvStatusSpec.text = STATUS
        Glide.with(binding.ivGameDetail.context).load(PIC).fitCenter().into(binding.ivGameDetail)
        binding.tvSinopsisSpec.text = SINOP
        binding.tvDevSpec.text = DEV
        binding.tvLaunchDateSpec.text = DATE
    }
}