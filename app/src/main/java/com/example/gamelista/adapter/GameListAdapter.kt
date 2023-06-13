package com.example.gamelista.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelista.Game
import com.example.gamelista.R

class GameListAdapter(
    private var gameList: List<Game>,
    private val onClickListener: (Game) -> Unit,
    private val onClickStarListener: (Game) -> Unit,
    private val onClickDeletedListener: (Game) -> Unit
) : RecyclerView.Adapter<GameListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return GameListViewHolder(
            layoutInflater.inflate(R.layout.item_game_list, parent, false),
            this
        )
    }

    override fun getItemCount(): Int = gameList.size

    override fun onBindViewHolder(holder: GameListViewHolder, position: Int) {
        val item = gameList[position]
        holder.render(
            item, onClickListener, onClickStarListener, onClickDeletedListener
        )
    }

    fun updateGames(gameList:List<Game>){
        this.gameList = gameList
        notifyDataSetChanged()
    }

}