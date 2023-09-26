package com.example.gamelista

import android.app.AlertDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamelista.adapter.GameListAdapter
import com.example.gamelista.databinding.FragmentFavBinding
import com.google.android.material.navigation.NavigationView

class FavFragment : Fragment() {

    lateinit var navigation: NavigationView
    private var _binding: FragmentFavBinding? = null
    private val binding get() = _binding!!
    private var myGameMutableList: MutableList<Game> = MyGameProvider.myGameList.toMutableList()
    private lateinit var adapter: GameListAdapter

//    private var mOnNavigationView = NavigationView.OnNavigationItemSelectedListener { item ->
//        when (item.itemId){
//            R.id.detailFragment -> {
//
//            }
//        }
//        false
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Toast.makeText(activity, "Segundo Fragment", Toast.LENGTH_SHORT).show()

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
        configFilter()
        initRecyclerView()
        configSwipe()
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
            val gameFiltered =
                myGameMutableList.filter { game ->
                    game.titulo.lowercase().contains(userFilter.toString().lowercase())
                }
            adapter.updateGames(gameFiltered)
        }
    }

    private fun initRecyclerView() {
        val llmanager = LinearLayoutManager(requireContext())
        adapter = GameListAdapter(
            gameList = myGameMutableList,
            onClickListener = { onItemSelected(it) },
            onClickStarListener = { onFavItem(it) },
            onClickDeletedListener = { onDeletedItem(it) },
            onAddToListListener = { game, status -> onListedItem(game, status) }
        )

        val decoration =
            DividerItemDecoration(binding.recyclerGameList.context, llmanager.orientation)
        binding.recyclerGameList.layoutManager = llmanager
        binding.recyclerGameList.adapter = adapter
        binding.recyclerGameList.addItemDecoration(decoration)
    }

    private fun onFavItem(game: Game) {
        MyGameProvider.myGameList.add(game)
    }

    private fun onItemSelected(game: Game) {
        //Toast.makeText(activity, game.titulo, Toast.LENGTH_SHORT).show()
        findNavController().navigate(
            R.id.action_favFragment2_to_detailFragment, bundleOf(
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

    private fun onListedItem(game: Game, status: GameStatus) {
        if (status == GameStatus.SIN_CLASIFICAR) {
            MyListProvider.deleteGame(game, status)
            //adapter.updateGames(MyListProvider.myListGameList)

        } else {
            MyListProvider.addOrUpdateGame(game, status)

        }
        adapter.notifyDataSetChanged()
    }

    private fun onDeletedItem(game: Game) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que quieres eliminar este juego de la lista de favoritos?")
            .setPositiveButton("Eliminar") { _, _ ->
                // Acción de eliminación del juego
                MyGameProvider.myGameList.remove(game)
                game.fav = false
                adapter.updateGames(MyGameProvider.myGameList)
            }
            .setNegativeButton("Cancelar", null)
            .create()
        alertDialog.show()
    }

}