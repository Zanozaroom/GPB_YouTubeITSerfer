package com.example.otusproject_ermoshina.ui.screen.playlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.otusproject_ermoshina.domain.model.YTPlayList
import com.example.otusproject_ermoshina.databinding.ItemPlaylistBinding

class AdapterPlayList(
    private val clickAdapterPlayList: ActionYTPlayList,
) : ListAdapter<YTPlayList, AdapterPlayList.YTPlayListViewHolder>(ExDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YTPlayListViewHolder {
        val binding =
            ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return YTPlayListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YTPlayListViewHolder, position: Int) {
        holder.populateYTPlayList(currentList[holder.absoluteAdapterPosition], clickAdapterPlayList)
    }

    class YTPlayListViewHolder(private val binding: ItemPlaylistBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun populateYTPlayList(
            positionVideo: YTPlayList,
            clickAdapterPlayList: ActionYTPlayList
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
                btnPositiveActionPlaylist.setOnClickListener {
                    clickAdapterPlayList.addToFavoritePL(positionVideo)
                }
            }

            Glide.with(binding.imageVideo)
                .load(positionVideo.imageList)
                .centerCrop()
                .into(binding.imageVideo)
        }
    }

    companion object {
        class ExDiffUtil : DiffUtil.ItemCallback<YTPlayList>() {
            override fun areItemsTheSame(oldItem: YTPlayList, newItem: YTPlayList): Boolean =
                oldItem.idList == newItem.idList

            override fun areContentsTheSame(oldItem: YTPlayList, newItem: YTPlayList): Boolean =
                oldItem == newItem
        }
    }
}