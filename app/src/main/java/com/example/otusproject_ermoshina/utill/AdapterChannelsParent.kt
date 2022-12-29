package com.example.otusproject_ermoshina.utill

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.base.YTChannelAndPlayList
import com.example.otusproject_ermoshina.databinding.ItemYtChanAndVideoBinding

class ExDiffUtilChannels: DiffUtil.ItemCallback<YTChannelAndPlayList>() {
    override fun areItemsTheSame(oldItem: YTChannelAndPlayList, newItem: YTChannelAndPlayList): Boolean = oldItem.idChannel == newItem.idChannel
    override fun areContentsTheSame(oldItem: YTChannelAndPlayList, newItem: YTChannelAndPlayList): Boolean = oldItem == newItem
}
class YTAdapterChannelsParent() : ListAdapter<YTChannelAndPlayList, YTAdapterChannelsParent.VideoViewHolder>(ExDiffUtilChannels()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoViewHolder {
        val binding = ItemYtChanAndVideoBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.populate(currentList[holder.absoluteAdapterPosition])
    }

    class VideoViewHolder(private val binding: ItemYtChanAndVideoBinding): RecyclerView.ViewHolder(binding.root){
        fun populate(positionVideo: YTChannelAndPlayList){

            binding.textChannel.text = positionVideo.titleChannel

        }

    }
}
