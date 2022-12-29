package com.example.otusproject_ermoshina.ui.fragment.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.otusproject_ermoshina.domain.model.YTSearch
import com.example.otusproject_ermoshina.databinding.ItemSearchBinding
import com.example.otusproject_ermoshina.ui.base.Formatter.Companion.dataFormatter

class ExDiffUtilSearchMain: DiffUtil.ItemCallback<YTSearch>() {
    override fun areItemsTheSame(oldItem: YTSearch, newItem: YTSearch): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: YTSearch, newItem: YTSearch): Boolean = oldItem == newItem
}
class AdapterSearch(private val itemClickListener: OnClickSearch):
    ListAdapter<YTSearch, AdapterSearch.VideoViewHolderSearch>(ExDiffUtilSearchMain()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VideoViewHolderSearch {
        val binding =
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolderSearch(binding)
    }

    override fun onBindViewHolder(
        holder: VideoViewHolderSearch,
        position: Int
    ) {
        holder.populate(currentList[position], itemClickListener)
    }

    class VideoViewHolderSearch(private val binding: ItemSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun populate(
            positionVideo: YTSearch,
            itemClickListener: OnClickSearch
        ) {
            binding.titleVideo.text = positionVideo.title
            binding.titleChannel.text = positionVideo.channelTitle
            binding.publishedAt.text = positionVideo.publishedAt.dataFormatter()
            if(positionVideo.description.isBlank()){
                binding.description.visibility = View.GONE
            }else{
                binding.description.visibility = View.VISIBLE
                binding.description.text = positionVideo.description
            }
            Glide.with(binding.imageVideo)
                .load(positionVideo.image)
                .into(binding.imageVideo)

            binding.btnOpenChannel.setOnClickListener {
                itemClickListener.openChannel(positionVideo.channelId)
            }
            binding.imageVideo.setOnClickListener {
                itemClickListener.openVideo(positionVideo.videoId)
            }
        }
    }
}
