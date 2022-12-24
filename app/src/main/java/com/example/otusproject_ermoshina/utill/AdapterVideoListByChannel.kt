package com.example.otusproject_ermoshina.utill

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.otusproject_ermoshina.base.YTPlayListOfChannel
import com.example.otusproject_ermoshina.databinding.ItemPlaylistBinding
import com.example.otusproject_ermoshina.ui.playlist.AddtoFavoritePlayList
import com.example.otusproject_ermoshina.ui.playlist.OpenPlayList


class AdapterVideoListByChannel(private val clickImage: OpenPlayList, private val addToFavoritePlayList: AddtoFavoritePlayList): ListAdapter<YTPlayListOfChannel, AdapterVideoListByChannel.VideoViewHolder>(ExDiffUtil()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return VideoViewHolder(binding)
    }
    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.populate(currentList[holder.absoluteAdapterPosition], clickImage,addToFavoritePlayList)
    }
    class VideoViewHolder(private val binding: ItemPlaylistBinding): RecyclerView.ViewHolder(binding.root){
        fun populate(
            positionVideo: YTPlayListOfChannel,
            clickImage: OpenPlayList,
            addToFavoritePlayList: AddtoFavoritePlayList){

            binding.titleVideo.text = positionVideo.titleListVideo
            binding.titleChannel.text = positionVideo.titleChannel

            binding.imageVideo.setOnClickListener{
                clickImage.openPlayList(positionVideo.idList)
            }
            binding.btnAddPlaylist.setOnClickListener {
                addToFavoritePlayList.addToFavorite(positionVideo)
            }

            Glide.with(binding.imageVideo)
                .load(positionVideo.imageList)
                .centerCrop()
                .into(binding.imageVideo)
        }
    }
    companion object{

        class ExDiffUtil() : DiffUtil.ItemCallback<YTPlayListOfChannel>() {
            override fun areItemsTheSame(oldItem: YTPlayListOfChannel, newItem: YTPlayListOfChannel): Boolean = oldItem.idList == newItem.idList
            override fun areContentsTheSame(oldItem: YTPlayListOfChannel, newItem: YTPlayListOfChannel): Boolean = oldItem == newItem
        }
    }
}