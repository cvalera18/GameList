package com.example.gamelista.ui.mylist

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamelista.model.GameStatus
import com.example.gamelista.R
import com.example.gamelista.adapter.GameListAdapter
import com.example.gamelista.databinding.FragmentMyListBinding
import com.example.gamelista.model.Game
import com.example.gamelista.model.MyListProvider


class MyListFragment : Fragment() {

    private var _binding: FragmentMyListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: GameListAdapter
    private val viewModel: MyListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configFilter()
        initRecyclerView()
        configSwipe()
        observeGameList()
        viewModel.getListGames()
    }

    private fun observeGameList() {
        viewModel.listedGameList.observe(viewLifecycleOwner) { listedGameList ->
            adapter.updateGames(listedGameList)
        }
    }

    private fun configSwipe() {

        binding.swipe.setColorSchemeResources(R.color.green, R.color.blueoscuro)
        binding.swipe.setOnRefreshListener {

            Handler(Looper.getMainLooper()).postDelayed({
                binding.swipe.isRefreshing = false
            }, 2000)
        }
    }

    private fun configFilter() {
        binding.etFilter.addTextChangedListener { userFilter ->
            viewModel.configFilter(userFilter.toString())
        }
    }

    private fun initRecyclerView() {
        adapter = GameListAdapter(
            gameList = emptyList(),
            onClickListener = { onItemSelected(it) },
            onClickStarListener = { onFavItem(it) }
        ) { game, status -> onListedItem(game, status) }

        val llmanager = LinearLayoutManager(requireContext())
        val decoration =
            DividerItemDecoration(binding.recyclerGameList.context, llmanager.orientation)
        binding.recyclerGameList.layoutManager = llmanager
        binding.recyclerGameList.adapter = adapter
        binding.recyclerGameList.addItemDecoration(decoration)
    }

    private fun onFavItem(game: Game) {
        viewModel.onFavItem(game)
    }

    private fun onListedItem(game: Game, status: GameStatus) {
        viewModel.onListedItem(game, status)
    }

    private fun onItemSelected(game: Game) {
        findNavController().navigate(
            R.id.action_myListFragment_to_detailFragment, bundleOf(
                "NAME" to game.titulo,
                "PLAT" to game.plataforma,
                "STATUS" to game.status,
                "PIC" to game.imagen,
                "SINOP" to game.sinopsis,
                "DEV" to game.dev,
                "FAV" to game.fav
            )
        )
    }
}