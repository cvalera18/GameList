package com.example.gamelista.ui.list

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelista.model.GameStatus
import com.example.gamelista.R
import com.example.gamelista.adapter.GameListAdapter
import com.example.gamelista.data.Repository
import com.example.gamelista.databinding.FragmentListBinding
import com.example.gamelista.model.Game

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: GameListAdapter
    private val viewModel: ListViewModel by viewModels()
    private var currentPage: Int = 1
    private var isLoading = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Repository.initialize(context.applicationContext)
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

//    override fun onResume() {
//        super.onResume()
//        viewModel.getListGames()
//    }

    private fun observeGameList() {
        viewModel.gameList.observe(viewLifecycleOwner) { gameList ->
            adapter.updateGames(gameList)
            isLoading = false
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
            onClickStarListener = { onFavItem(it) }
        ) { game, status -> onListedItem(game, status) }

        val llmanager = LinearLayoutManager(requireContext())

        val decoration = DividerItemDecoration(activity, llmanager.orientation)
        binding.recyclerGameList.layoutManager = llmanager
        binding.recyclerGameList.adapter = adapter
        binding.recyclerGameList.addItemDecoration(decoration)

//         Agregar ScrollListener para cargar más juegos al llegar al final de la lista
        binding.recyclerGameList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount: Int = llmanager.childCount
                val pastVisibleItem: Int = llmanager.findLastCompletelyVisibleItemPosition()
                val total = adapter.itemCount
                if (visibleItemCount + pastVisibleItem >= total) {
                    // Solo cargar más juegos si no hay una carga en progreso
                    if (!isLoading) {
                        isLoading = true
                        viewModel.pasarPagina()
                        viewModel.getListGames()
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun onFavItem(game: Game) {
        viewModel.onFavItem(game)
    }

    private fun onListedItem(game: Game, status: GameStatus) {
        viewModel.onListedItem(game, status)
    }

    private fun onItemSelected(game: Game) {
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