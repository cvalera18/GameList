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
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamelista.adapter.GameListAdapter
import com.example.gamelista.databinding.FragmentFavBinding

class FavFragment : Fragment() {

    private var _binding: FragmentFavBinding? = null
    private val binding get() = _binding!!
    private var myGameMutableList: MutableList<Game> = MyGameProvider.myGameList.toMutableList()
    private lateinit var adapter: GameListAdapter
    private val llmanager = LinearLayoutManager(activity)

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
        adapter = GameListAdapter(
            gameList = myGameMutableList,
            onClickListener = { onItemSelected(it) },
            onClickStarListener = { onFavItem(it) },
            onClickDeletedListener = { onDeletedItem(it) }
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
        Toast.makeText(activity, game.titulo, Toast.LENGTH_SHORT).show()
    }

//    private fun onDeletedItem(game: Game) {
//        MyGameProvider.myGameList.remove(game)
//        //adapter.notifyDataSetChanged()
//        adapter.updateGames(MyGameProvider.myGameList)
//    }

    private fun onDeletedItem(game: Game) {
        val alertDialog = AlertDialog.Builder(context)
            .setTitle("Confirmar eliminación")
            .setMessage("¿Estás seguro de que quieres eliminar este juego de la lista de favoritos?")
            .setPositiveButton("Eliminar") { _, _ ->
                // Acción de eliminación del juego
                MyGameProvider.myGameList.remove(game)
                game.fav=false
                adapter.updateGames(MyGameProvider.myGameList)
            }
            .setNegativeButton("Cancelar", null)
            .create()
        alertDialog.show()
    }

}