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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamelista.adapter.GameListAdapter
import com.example.gamelista.databinding.FragmentFavBinding
import com.example.gamelista.databinding.FragmentMyListBinding


class MyListFragment : Fragment() {

    private var _binding: FragmentMyListBinding? = null
    private val binding get() = _binding!!
    private var myListMutableList: MutableList<Game> = MyListProvider.myListGameList.toMutableList()
    private lateinit var adapter: GameListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Toast.makeText(activity, "Segundo Fragment", Toast.LENGTH_SHORT).show()

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
                myListMutableList.filter { game ->
                    game.titulo.lowercase().contains(userFilter.toString().lowercase())
                }
            adapter.updateGames(gameFiltered)
        }
    }

    private fun initRecyclerView() {
        val llmanager = LinearLayoutManager(requireContext())
        adapter = GameListAdapter(
            gameList = myListMutableList,
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

    private fun onListedItem(game: Game, status: GameStatus) {
        if (status == GameStatus.SIN_CLASIFICAR){
            MyListProvider.deleteGame(game, status)
            adapter.updateGames(MyListProvider.myListGameList)

        } else {
            MyListProvider.addOrUpdateGame(game, status)
//            adapter.notifyDataSetChanged()

        }
        adapter.notifyDataSetChanged()
    }

    private fun onItemSelected(game: Game) {
        //Toast.makeText(activity, game.titulo, Toast.LENGTH_SHORT).show()
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

    private fun onDeletedItem(game: Game) {
        MyGameProvider.myGameList.remove(game)
        game.fav = false
        adapter.notifyDataSetChanged()
    }

}