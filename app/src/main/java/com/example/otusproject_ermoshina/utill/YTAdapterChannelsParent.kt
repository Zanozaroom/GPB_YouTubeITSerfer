package com.example.otusproject_ermoshina.utill

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.R
import com.example.otusproject_ermoshina.base.ChannelAndListVideos
import com.example.otusproject_ermoshina.databinding.ItemYtChanAndVideoBinding
import com.example.otusproject_ermoshina.ui.OnClickButtonsChannel

class ExDiffUtilChannels() : DiffUtil.ItemCallback<ChannelAndListVideos>() {
    override fun areItemsTheSame(oldItem: ChannelAndListVideos, newItem: ChannelAndListVideos): Boolean = oldItem == newItem
    override fun areContentsTheSame(oldItem: ChannelAndListVideos, newItem: ChannelAndListVideos): Boolean = oldItem == newItem
}
class YTAdapterChannelsParent(private val onChannelClick: OnClickButtonsChannel) : ListAdapter<ChannelAndListVideos, YTAdapterChannelsParent.VideoViewHolder>(ExDiffUtilChannels()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_yt_chan_and_video, parent,false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.populate(currentList[holder.absoluteAdapterPosition], onChannelClick)
    }

    class VideoViewHolder(private val view: View): RecyclerView.ViewHolder(view){
        fun populate(positionVideo: ChannelAndListVideos, itemClickListener: OnClickButtonsChannel){
            val binding = ItemYtChanAndVideoBinding.bind(view)
            binding.openPageThisChannel.setOnClickListener {
                itemClickListener.onClickOpenAllPlayListsChannel(positionVideo.idChannel)
            }
            binding.findMoreChannels.setOnClickListener {
                itemClickListener.onClickFindLikeThisChannel(positionVideo.idChannel)
            }
            binding.textChannel.text = positionVideo.titleChannel

            val childAdapter = YTAdapterChannelChild{
                    itemClickListener.onClickOnImage(it.idList)
            }
            childAdapter.submitList(positionVideo.listVideos)
            binding.childRW.apply {
                adapter = childAdapter
                 addItemDecoration(DecoratorChildChannels(childAdapter.currentList.size,context))
            }


        }

    }
}
