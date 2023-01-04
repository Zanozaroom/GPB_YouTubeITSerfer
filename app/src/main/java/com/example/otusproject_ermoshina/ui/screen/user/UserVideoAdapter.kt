package com.example.otusproject_ermoshina.ui.screen.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.otusproject_ermoshina.domain.model.YTVideo
import com.example.otusproject_ermoshina.databinding.ItemVideolistUserBinding
import com.example.otusproject_ermoshina.ui.screen.user.UserVideoAdapter.VideoUserViewHolder.Companion.ExDiffUtilUserVideo


class UserVideoAdapter(private val onClickVideoUser: UserVideoAction): ListAdapter<YTVideo, UserVideoAdapter.VideoUserViewHolder>(
    ExDiffUtilUserVideo()
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
                binding.buttonShowVideo.setOnClickListener {
                    onClickVideoUser.openVideo(positionVideo.idVideo)
                }
                binding.titleChannel.setOnClickListener {
                    onClickVideoUser.openChannel(positionVideo.channelId)
                }
                Glide.with(imageVideo).load(positionVideo.image).into(imageVideo)
            }
        }

        companion object {
            class ExDiffUtilUserVideo() : DiffUtil.ItemCallback<YTVideo>() {
                override fun areItemsTheSame(oldItem: YTVideo, newItem: YTVideo): Boolean =
                    oldItem.idVideo == newItem.idVideo

                override fun areContentsTheSame(oldItem: YTVideo, newItem: YTVideo): Boolean =
                    oldItem == newItem
            }
        }
    }


}