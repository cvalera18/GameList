package com.example.gamelista.ui.favorites

import android.app.AlertDialog
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
import com.example.gamelista.model.GameStatus
import com.example.gamelista.R
import com.example.gamelista.adapter.GameListAdapter
import com.example.gamelista.data.Repository
import com.example.gamelista.databinding.FragmentFavBinding
import com.example.gamelista.model.Game
import com.example.gamelista.model.FavGameProvider

class FavFragment : Fragment() {

    private var _binding: FragmentFavBinding? = null
    private val binding get() = _binding!!
    private var myGameMutableList: MutableList<Game> = FavGameProvider.modelFavGameList.toMutableList()
    private lateinit var adapter: GameListAdapter
    private var isLoading = false

    private val viewModel: FavViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Repository.initialize(context.applicationContext)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchInList()
        initRecyclerView()
        configSwipe()
        observeFavGameList()
        viewModel.getListGames()
    }

    override fun onResume() {
    super.onResume()
    viewModel.getListGames()
    }

    private fun observeFavGameList() {
        viewModel.favGameList.observe(viewLifecycleOwner) { favGameList ->
            adapter.updateGames(favGameList)
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

//            Handler(Looper.getMainLooper()).postDelayed({
//                binding.swipe.isRefreshing = false
//            }, 2000)
        }
    }

    private fun searchInList() {
        binding.etFilter.addTextChangedListener { userFilter ->
            viewModel.searchInList(userFilter.toString())
        }
    }

    private fun initRecyclerView() {
        val llmanager = LinearLayoutManager(requireContext())
        adapter = GameListAdapter(
            gameList = emptyList(),
            onClickListener = { onItemSelected(it) },
            onClickStarListener = { onFavItem(it) }
        ) { game, status -> onListedItem(game, status) }

        val decoration =
            DividerItemDecoration(binding.recyclerGameList.context, llmanager.orientation)
        binding.recyclerGameList.layoutManager = llmanager
        binding.recyclerGameList.adapter = adapter
//        binding.recyclerGameList.addItemDecoration(decoration)
    }

    private fun onFavItem(game: Game) {

        if (!game.fav){
            viewModel.onFavItem(game)
        } else {
            val alertDialog = AlertDialog.Builder(context)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Estás seguro de que quieres eliminar este juego de la lista de favoritos?")
                .setPositiveButton("Eliminar") { _, _ ->
                    // Acción de eliminación del juego
                    viewModel.unFavItem(game)
                }
                .setNegativeButton("Cancelar", null)
                .create()
            alertDialog.show()
        }
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

        findNavController().navigate(R.id.action_favFragment2_to_detailFragment, bundle)
    }

    private fun onListedItem(game: Game, status: GameStatus) {
        viewModel.onListedItem(game, status)
    }

}