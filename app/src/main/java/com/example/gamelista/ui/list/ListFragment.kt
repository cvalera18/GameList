package com.example.gamelista.ui.list

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
import com.example.gamelista.databinding.FragmentListBinding
import com.example.gamelista.model.Game
import com.example.gamelista.model.GameProvider
import com.example.gamelista.model.MyGameProvider
import com.example.gamelista.model.MyListProvider

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var gameMutableList: MutableList<Game> = GameProvider.modelGameList.toMutableList()
    private lateinit var adapter: GameListAdapter
    private val viewModel: ListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
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
        viewModel.gameList.observe(viewLifecycleOwner) { gameList ->
            adapter.updateGames(gameList)
        }
    }

    private fun configSwipe() {

        binding.swipe.setColorSchemeResources(R.color.grey, R.color.blueoscuro)
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
            onClickStarListener = { onFavItem(it) },
            onClickDeletedListener = { },
            onAddToListListener = { game, status -> onListedItem(game, status) }
        )

        val llmanager = LinearLayoutManager(requireContext())

        val decoration = DividerItemDecoration(activity, llmanager.orientation)
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
        //Toast.makeText(activity, game.titulo, Toast.LENGTH_SHORT).show()
        findNavController().navigate(
            R.id.action_listFragment_to_detailFragment, bundleOf(
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