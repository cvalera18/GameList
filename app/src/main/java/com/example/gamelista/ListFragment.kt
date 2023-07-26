package com.example.gamelista

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
import com.example.gamelista.databinding.FragmentListBinding

class ListFragment : Fragment() {
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private var gameMutableList: MutableList<Game> = GameProvider.gameList.toMutableList()
    private lateinit var adapter: GameListAdapter
    private val llmanager = LinearLayoutManager(activity)

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
            val gameFiltered =
                gameMutableList.filter { game ->
                    game.titulo.lowercase().contains(userFilter.toString().lowercase())
                }
            adapter.updateGames(gameFiltered)
        }
    }

    private fun initRecyclerView() {
        adapter = GameListAdapter(
            gameList = gameMutableList,
            onClickListener = { onItemSelected(it) },
            onClickStarListener = { onFavItem(it) },
            onClickDeletedListener = { onDeletedItem(it) },
            onAddToListListener = { game, status -> onListedItem(game, status) }
        )


        val decoration = DividerItemDecoration(activity, llmanager.orientation)
        binding.recyclerGameList.layoutManager = llmanager
        binding.recyclerGameList.adapter = adapter
        binding.recyclerGameList.addItemDecoration(decoration)

    }

    private fun onFavItem(game: Game) {
        MyGameProvider.myGameList.add(game)
        adapter.notifyDataSetChanged()
    }

    private fun onListedItem(game: Game, status: GameStatus) {
        if (status != GameStatus.SIN_CLASIFICAR){
            MyListProvider.addOrUpdateGame(game, status)
        } else {
            MyListProvider.deleteGame(game, status)
        }
        adapter.notifyDataSetChanged()
    }

    private fun onDeletedItem(game: Game) {
        MyGameProvider.myGameList.remove(game)
        game.fav = false
        adapter.notifyDataSetChanged()
    }


    private fun onItemSelected(game: Game) {
        Toast.makeText(activity, game.titulo, Toast.LENGTH_SHORT).show()
    }
}