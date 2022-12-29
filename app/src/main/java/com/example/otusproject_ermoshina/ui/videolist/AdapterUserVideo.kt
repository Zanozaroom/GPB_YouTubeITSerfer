package com.example.otusproject_ermoshina.ui.videolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.base.YTVideo
import com.example.otusproject_ermoshina.databinding.ItemVideolistUserBinding
import com.example.otusproject_ermoshina.ui.video.UserVideoAction
import com.example.otusproject_ermoshina.ui.videolist.AdapterUserVideo.VideoUserViewHolder.Companion.ExDiffUtilVideoUser


class AdapterUserVideo(private val onClickVideoUser: UserVideoAction): ListAdapter<YTVideo, AdapterUserVideo.VideoUserViewHolder>(
    ExDiffUtilVideoUser()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoUserViewHolder {
        val binding = ItemVideolistUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
       return VideoUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoUserViewHolder, position: Int) {
        holder.populate(currentList[holder.absoluteAdapterPosition], onClickVideoUser)
    }

    class VideoUserViewHolder(private val binding: ItemVideolistUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populate(
            positionVideo: YTVideo,
            onClickVideoUser: UserVideoAction
        ) {
            with(binding){

                imageVideo.setOnClickListener {
                    onClickVideoUser.openVideo(positionVideo.idVideo)
                }
                titleVideo.text = positionVideo.title
                titleChannel.text = positionVideo.channelTitle
                description.text = positionVideo.description
                actionNegativeButton.setOnClickListener {
                    onClickVideoUser.deleteVideo(positionVideo)
                }
            }
        }

        companion object {
            class ExDiffUtilVideoUser() : DiffUtil.ItemCallback<YTVideo>() {
                override fun areItemsTheSame(oldItem: YTVideo, newItem: YTVideo): Boolean =
                    oldItem.idVideo == newItem.idVideo

                override fun areContentsTheSame(oldItem: YTVideo, newItem: YTVideo): Boolean =
                    oldItem == newItem
            }
        }
    }


}