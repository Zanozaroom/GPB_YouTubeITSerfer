package com.example.otusproject_ermoshina.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.otusproject_ermoshina.base.YTSearch
import com.example.otusproject_ermoshina.databinding.ItemYtChannelsBinding


class ExDiffUtilChild: DiffUtil.ItemCallback<YTSearch>() {
    override fun areItemsTheSame(oldItem: YTSearch, newItem: YTSearch): Boolean =
        oldItem.videoId == newItem.videoId

    override fun areContentsTheSame(oldItem: YTSearch, newItem: YTSearch): Boolean =
        oldItem == newItem
}

class YTAdapterChild(val onclickChild: OnClickYTListener) :
    ListAdapter<YTSearch, YTAdapterChild.VideoViewHolderChild>(ExDiffUtilChild()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoViewHolderChild {
        val binding = ItemYtChannelsBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return VideoViewHolderChild(binding)
    }
    override fun onBindViewHolder(holder: VideoViewHolderChild, position: Int) {
        holder.populate(currentList[holder.absoluteAdapterPosition], onclickChild)
    }
    class VideoViewHolderChild(private val binding: ItemYtChannelsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun populate(positionVideo: YTSearch, itemClickListener: OnClickYTListener) {
            itemView.setOnClickListener {
                itemClickListener.onClickOnImage(positionVideo.videoId)
            }
            binding.btnOpenChannel.setOnClickListener {
                itemClickListener.onClickOpenChannel(positionVideo.channelId)
            }

            binding.textTitle.text = positionVideo.title
            Glide.with(binding.image)
                .load(positionVideo.image)
                .centerCrop()
                .into(binding.image)
        }
    }
}