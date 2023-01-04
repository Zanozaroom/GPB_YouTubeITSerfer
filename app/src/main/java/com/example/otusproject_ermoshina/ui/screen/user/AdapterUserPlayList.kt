package com.example.otusproject_ermoshina.ui.screen.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.databinding.ItemPlaylistUserBinding

class AdapterUserPlayList(
    private val clickAdapterPlayList: ActionUserPlayList
) : ListAdapter<YTPlayList, AdapterUserPlayList.VideoViewHolder>(ExDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding =
            ItemPlaylistUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.populateUserPlayList(currentList[holder.absoluteAdapterPosition], clickAdapterPlayList)
    }

    class VideoViewHolder(private val binding: ItemPlaylistUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populateUserPlayList(
            positionVideo: YTPlayList,
            clickAdapterPlayList: ActionUserPlayList
        ) {
            with(binding) {
                titleVideo.text = positionVideo.titleListVideo
                titleChannel.text = positionVideo.titleChannel
                imageVideo.setOnClickListener {
                    clickAdapterPlayList.openPlayList(positionVideo.idList)
                }
                btnOpenThisPL.setOnClickListener {
                    clickAdapterPlayList.openPlayList(positionVideo.idList)
                }
                titleChannel.setOnClickListener {
                    clickAdapterPlayList.openChannel(positionVideo.idChannel)
                }
                btnNegativeActionPlaylist.setOnClickListener {
                    clickAdapterPlayList.deleteFromFavoritePL(positionVideo.idList)
                }
            }

            Glide.with(binding.imageVideo)
                .load(positionVideo.imageList)
                .centerCrop()
                .into(binding.imageVideo)
        }

    }
    companion object {
        class ExDiffUtil: DiffUtil.ItemCallback<YTPlayList>() {
            override fun areItemsTheSame(oldItem: YTPlayList, newItem: YTPlayList): Boolean =
                oldItem.idList == newItem.idList
            override fun areContentsTheSame(oldItem: YTPlayList, newItem: YTPlayList): Boolean =
                oldItem == newItem
        }
    }
}