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
import com.example.otusproject_ermoshina.databinding.ItemYtChanPlaylistsBinding

typealias onClick = (idVideo: PlayListOfChannel) -> Unit

class ExDiffUtil() : DiffUtil.ItemCallback<PlayListOfChannel>() {
    override fun areItemsTheSame(oldItem: PlayListOfChannel, newItem: PlayListOfChannel): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: PlayListOfChannel, newItem: PlayListOfChannel): Boolean = oldItem == newItem
}
class YTAdapterVideoIdList(val itemClickListener: onClick): ListAdapter<PlayListOfChannel, YTAdapterVideoIdList.VideoViewHolder>(ExDiffUtil()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_yt_chan_playlists, parent,false)
        return VideoViewHolder(view)
    }
    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.populate(currentList[holder.absoluteAdapterPosition], itemClickListener)
    }
    class VideoViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        fun populate(positionVideo: PlayListOfChannel, itemClickListener: onClick){
            itemView.setOnClickListener{
                itemClickListener(positionVideo)
            }
            val binding = ItemYtChanPlaylistsBinding.bind(view)
            binding.titleVideoList.text = positionVideo.titleListVideo
            Glide.with(binding.imageVideo)
                .load(positionVideo.imageList)
                .centerCrop()
                .into(binding.imageVideo)
        }
    }
}