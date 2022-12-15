package com.example.otusproject_ermoshina.utill

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.base.PlayListOfChannel
import com.example.otusproject_ermoshina.databinding.ItemYtChannelsBinding

typealias OnChannelChildClick = (idVideoList: PlayListOfChannel) -> Unit

class ExDiffUtilChannelsChild() : DiffUtil.ItemCallback<PlayListOfChannel>() {
    override fun areItemsTheSame(oldItem: PlayListOfChannel, newItem: PlayListOfChannel): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: PlayListOfChannel, newItem: PlayListOfChannel): Boolean =
        oldItem == newItem
}

class YTAdapterChannelChild(val onChannelClick: OnChannelChildClick) :
    ListAdapter<PlayListOfChannel, YTAdapterChannelChild.VideoViewHolderChild>(ExDiffUtilChannelsChild()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoViewHolderChild {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_yt_channels, parent, false)
        return VideoViewHolderChild(view)
    }
    override fun onBindViewHolder(holder: VideoViewHolderChild, position: Int) {
        holder.populate(currentList[holder.absoluteAdapterPosition], onChannelClick)
    }
    class VideoViewHolderChild(private val view: View) : RecyclerView.ViewHolder(view) {
        fun populate(positionVideo: PlayListOfChannel, itemClickListener: OnChannelChildClick) {
            itemView.setOnClickListener {
                itemClickListener(positionVideo)
            }
            val binding = ItemYtChannelsBinding.bind(view)
            //TODO добавить данные
            binding.nameChannel.text = positionVideo.titleListVideo
            Glide.with(binding.imageChannel)
                .load(positionVideo.imageList)
                .centerCrop()
                .into(binding.imageChannel)
        }
    }
}