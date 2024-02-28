package com.example.gamelista.adapter

import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.gamelista.model.Game
import com.example.gamelista.model.GameStatus
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
        onAddToListListener: (Game, status: GameStatus) -> Unit
    ) {

        binding.tvGame.text = gameListModel.titulo
        binding.tvStatus.text = gameListModel.status.value

        cargarImagenesPlataforma(
            gameListModel.plataforma,
            binding.ivPlatLogo,
            binding.ivPlatLogo2,
            binding.ivPlatLogo3,
            binding.ivPlatLogo4,
            binding.ivPlatLogo5,
            binding.ivPlatLogo6
        )

        Glide.with(binding.ivGame.context).load(gameListModel.imagen).optionalFitCenter()
            .optionalCenterCrop().into(binding.ivGame)
        itemView.setOnClickListener { onClickListener(gameListModel) }
        binding.statusCard.setOnClickListener { showPopup(gameListModel, onAddToListListener) }

        if (gameListModel.status != GameStatus.SIN_CLASIFICAR) {
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
        binding.favCardView.setOnClickListener {
            onClickStarListener.invoke(gameListModel)
        }
    }

    private fun cargarImagenPlataforma(imageView: ImageView, plataforma: String) {
        Glide.with(imageView.context)
            .load(plataforma)
            .fitCenter()
            .into(imageView)
    }

    private fun cargarImagenesPlataforma(plataformas: List<String>, vararg imageViews: ImageView) {
        for ((index, plataforma) in plataformas.withIndex()) {
            if (index < imageViews.size) {
                cargarImagenPlataforma(imageViews[index], plataforma)
            } else {
                break // Si no hay más ImageView disponibles, sal del bucle
            }
        }
    }

    private fun showPopup(game: Game, onAddToListListener: (Game, status: GameStatus) -> Unit) {
        val popupMenu = PopupMenu(binding.ivCircle.context, binding.ivCircle)
        popupMenu.menuInflater.inflate(R.menu.status_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.status_playing -> {
                    onAddToListListener.invoke(game, GameStatus.JUGANDO)
                    true
                }

                R.id.status_completed -> {
                    onAddToListListener.invoke(game, GameStatus.COMPLETADO)
                    true
                }

                R.id.status_sinclasificar -> {
//                    game.setStatusGame("Sin Clasificar")
                    onAddToListListener.invoke(game, GameStatus.SIN_CLASIFICAR)
//                    adapter.notifyDataSetChanged()
                    true
                }

                R.id.status_pendiente -> {
                    onAddToListListener.invoke(game, GameStatus.PENDIENTE)
                    true
                }

                R.id.status_abandonado -> {
                    onAddToListListener.invoke(game, GameStatus.ABANDONADO)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

}
