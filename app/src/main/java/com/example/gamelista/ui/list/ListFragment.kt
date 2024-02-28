package com.example.gamelista.ui.list

import android.content.Context
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getListGames()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configFilter()
        initRecyclerView()
        configSwipe()
        observeGameList()
        getListGames()
    }

    private fun getListGames() {
        viewModel.getListGames()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListGames()
        isLoading = false
    }



    private fun observeGameList() {
        viewModel.gameList.observe(viewLifecycleOwner) { gameList ->
                adapter.updateGames(gameList)
        }
    }

    private fun configSwipe() {

//        binding.swipe.setColorSchemeResources(R.color.md_theme_outline_highContrast, R.color.md_theme_primary_highContrast)
        binding.swipe.setOnRefreshListener {
            if (!isLoading) {
                isLoading = true
                binding.swipe.isRefreshing = true
                viewModel.getListGames()
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.swipe.isRefreshing = false
                    isLoading = false
                }, 2000)
            }
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
                        binding.swipe.isRefreshing = true
                        viewModel.pasarPagina()
                        viewModel.getListGames()
                        Handler(Looper.getMainLooper()).postDelayed({
                            binding.swipe.isRefreshing = false
                            isLoading = false
                        }, 1500)
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
        val bundle = Bundle().apply {
            putString("NAME", game.titulo)
            putString("STATUS", game.status.value)
            putString("PIC", game.imagen)
            putString("SINOP", game.sinopsis)
            putString("DEV", game.dev)
            putBoolean("FAV", game.fav)
            putString("DATE", game.release_date)
        }

        if (game.plataforma.size >= 1) {
            bundle.putString("PLAT", game.plataforma[0])
        }
        if (game.plataforma.size >= 2) {
            bundle.putString("PLAT2", game.plataforma[1])
        }
        if (game.plataforma.size >= 3) {
            bundle.putString("PLAT3", game.plataforma[2])
        }
        if (game.plataforma.size >= 4) {
            bundle.putString("PLAT4", game.plataforma[3])
        }
        if (game.plataforma.size >= 5) {
            bundle.putString("PLAT5", game.plataforma[4])
        }
        if (game.plataforma.size >= 6) {
            bundle.putString("PLAT6", game.plataforma[5])
        }

        findNavController().navigate(R.id.action_listFragment_to_detailFragment, bundle)
    }
}