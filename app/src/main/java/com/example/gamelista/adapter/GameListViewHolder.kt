package com.example.gamelista.adapter

import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.gamelista.Game
import com.example.gamelista.R
import com.example.gamelista.databinding.ItemGameListBinding

class GameListViewHolder(
    view: View,
    private val adapter: GameListAdapter
) : ViewHolder(view) {

    private val binding = ItemGameListBinding.bind(view)

    fun render(
        gameListModel: Game,
        onClickListener: (Game) -> Unit,
        onClickStarListener: (Game) -> Unit,
        onClickDeletedListener: (Game) -> Unit,
        onAddToListListener: (Game) -> Unit
    ) {

        binding.tvGame.text = gameListModel.titulo
        binding.tvStatus.text = gameListModel.status
        binding.tvPlatform.text = gameListModel.plataforma
        Glide.with(binding.ivGame.context).load(gameListModel.imagen).into(binding.ivGame)
        itemView.setOnClickListener { onClickListener(gameListModel) }
        binding.ivCircle.setOnClickListener { showPopup(gameListModel, onAddToListListener) }

        if (gameListModel.status!="Sin Clasificar"){
            binding.ivCircle.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.ivCircle.context,
                    R.drawable.check_circle_outline_24
                )
            )
        } else {
            binding.ivCircle.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.ivCircle.context,
                    R.drawable.bx_circle
                )
            )
        }

        if (!gameListModel.fav) {
            binding.ivStar.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.ivGame.context,
                    R.drawable.baseline_star_outline_24
                )
            )
        } else {
            binding.ivStar.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.ivGame.context,
                    R.drawable.baseline_star_24
                )
            )
        }

        //Listener de la estrella
        binding.ivStar.setOnClickListener {

            if (!gameListModel.fav) {
                gameListModel.fav = true
                onClickStarListener.invoke(gameListModel)
                binding.ivStar.setImageDrawable(it.context.getDrawable(R.drawable.baseline_star_24))
            } else {
                onClickDeletedListener(gameListModel)
            }
        }


    }


    private fun showPopup(game: Game, onAddToListListener: (Game) -> Unit) {
        val popupMenu = PopupMenu(binding.ivCircle.context, binding.ivCircle)
        popupMenu.menuInflater.inflate(R.menu.status_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.status_playing -> {
                    game.setStatusGame("Jugando")
                    onAddToListListener.invoke(game)
                    adapter.notifyDataSetChanged()
                    true
                }

                R.id.status_completed -> {
                    game.setStatusGame("Completado")
                    onAddToListListener.invoke(game)
                    adapter.notifyDataSetChanged()
                    true
                }

                R.id.status_sinclasificar -> {
                    game.setStatusGame("Sin Clasificar")
                    onAddToListListener.invoke(game)
                    adapter.notifyDataSetChanged()
                    true
                }

                R.id.status_pendiente -> {
                    game.setStatusGame("Pendiente")
                    onAddToListListener.invoke(game)
                    adapter.notifyDataSetChanged()
                    true
                }

                R.id.status_abandonado -> {
                    game.setStatusGame("Abandonado")
                    onAddToListListener.invoke(game)
                    adapter.notifyDataSetChanged()
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

}
