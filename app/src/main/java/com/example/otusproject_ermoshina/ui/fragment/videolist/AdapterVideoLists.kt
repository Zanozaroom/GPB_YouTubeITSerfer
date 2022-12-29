package com.example.otusproject_ermoshina.ui.fragment.videolist


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.otusproject_ermoshina.domain.model.YTVideoList
import com.example.otusproject_ermoshina.databinding.ItemVideolistBinding
import com.example.otusproject_ermoshina.ui.base.Formatter.Companion.dataFormatter

typealias OnClickImageVideoList = (item: String) -> Unit

class AdapterVideoLists(
    private val onClickListener: OnClickVideoList
):
    ListAdapter<YTVideoList, AdapterVideoLists.VideoListViewHolder>(
        ExDiffUtilVideoListByIdList()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoListViewHolder {
        val binding = ItemVideolistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VideoListViewHolder, position: Int) {
        holder.populate(currentList[holder.absoluteAdapterPosition], onClickListener)
    }

    class VideoListViewHolder(private val binding: ItemVideolistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun populate(positionVideo: YTVideoList,
                     onClickListener: OnClickVideoList) {

            with(binding){
                titleVideo.text = positionVideo.title
                titleChannel.text = positionVideo.channelTitle
                when{
                    positionVideo.description.isBlank() -> description.visibility = View.GONE
                    else -> {
                        description.visibility = View.VISIBLE
                        description.text = positionVideo.description
                    }
                }
                when{
                    positionVideo.videoPublishedAt.isBlank() -> publishedAt.visibility = View.INVISIBLE
                    else -> publishedAt.text = positionVideo.videoPublishedAt.dataFormatter()
                }

                binding.imageVideo.setOnClickListener{
                    onClickListener.onClickImage(positionVideo.idVideo)
                }
                binding.actionPositiveButton.setOnClickListener {
                    onClickListener.onClickAddVideoToFavorite(positionVideo.idVideo)
                }
                binding.iconGoWatch.setOnClickListener {
                    onClickListener.onClickIconOpenVideo(positionVideo.idVideo)
                }
                Glide.with(imageVideo)
                    .load(positionVideo.image)
                    .centerCrop()
                    .into(imageVideo)
            }
            }
        }

companion object{
    class ExDiffUtilVideoListByIdList() : DiffUtil.ItemCallback<YTVideoList>() {
        override fun areItemsTheSame(oldItem: YTVideoList, newItem: YTVideoList): Boolean =
            oldItem.idVideo == newItem.idVideo

        override fun areContentsTheSame(oldItem: YTVideoList, newItem: YTVideoList): Boolean =
            oldItem == newItem
    }
}
    }
