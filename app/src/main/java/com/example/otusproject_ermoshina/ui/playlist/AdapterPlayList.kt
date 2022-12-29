package com.example.otusproject_ermoshina.ui.playlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.otusproject_ermoshina.base.YTPlayList
import com.example.otusproject_ermoshina.databinding.ItemPlaylistBinding
import com.example.otusproject_ermoshina.ui.user_pageview.UserPlayList

open class AdapterPlayList(
    private val clickAdapterPlayList: ActionPL,
    private val fragment:BasePLFragment
) : ListAdapter<YTPlayList, AdapterPlayList.VideoViewHolder>(ExDiffUtil()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding =
            ItemPlaylistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        holder.populate(fragment,currentList[holder.absoluteAdapterPosition], clickAdapterPlayList)
        }

    class VideoViewHolder(private val binding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populate(
            fragment:BasePLFragment,
            positionVideo: YTPlayList,
            clickAdapterPlayList: ActionPL
        ) {
            when(fragment){
                is UserPlayList -> setUserPLActionBtn(clickAdapterPlayList, positionVideo)
                is YTPlayListFragment -> setYTPLActionBtn(clickAdapterPlayList, positionVideo)
            }
            with(binding){
                titleVideo.text = positionVideo.titleListVideo
                titleChannel.text = positionVideo.titleChannel
                imageVideo.setOnClickListener {
                    clickAdapterPlayList.openPL(positionVideo.idList)
                }
            }

            Glide.with(binding.imageVideo)
                .load(positionVideo.imageList)
                .centerCrop()
                .into(binding.imageVideo)
        }

        private fun setUserPLActionBtn(clickAdapterPlayList: ActionPL, positionVideo: YTPlayList){
            with(binding){
                btnNegativeActionPlaylist.visibility = View.VISIBLE
                btnPositiveActionPlaylist.visibility=View.GONE
                btnNegativeActionPlaylist.setOnClickListener {
                    clickAdapterPlayList.removeFavorite(positionVideo)
                }
            }
        }
        private fun setYTPLActionBtn(clickAdapterPlayList: ActionPL, positionVideo: YTPlayList){
            with(binding){
                btnNegativeActionPlaylist.visibility = View.GONE
                btnPositiveActionPlaylist.visibility=View.VISIBLE
                btnPositiveActionPlaylist.setOnClickListener {
                    clickAdapterPlayList.addToFavoritePL(positionVideo)
                }
            }
        }

    }
    companion object {
        class ExDiffUtil() : DiffUtil.ItemCallback<YTPlayList>() {
            override fun areItemsTheSame(oldItem: YTPlayList, newItem: YTPlayList): Boolean =
                oldItem.idList == newItem.idList
            override fun areContentsTheSame(oldItem: YTPlayList, newItem: YTPlayList): Boolean =
                oldItem == newItem
        }
    }
}