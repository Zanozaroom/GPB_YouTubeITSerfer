package com.example.otusproject_ermoshina.ui.screen.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.domain.model.YTMainFragmentData
import com.example.otusproject_ermoshina.databinding.ItemMainFragmentBinding
import com.example.otusproject_ermoshina.utill.DecoratorChildChannels


class ExDiffUtilMain: DiffUtil.ItemCallback<YTMainFragmentData>() {
    override fun areItemsTheSame(oldItem: YTMainFragmentData, newItem: YTMainFragmentData): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: YTMainFragmentData, newItem: YTMainFragmentData): Boolean = oldItem == newItem
}
class AdapterMainParent(private val onChannelClick: OnClickYTListener) : ListAdapter<YTMainFragmentData, AdapterMainParent.ViewHolderMain>(
    ExDiffUtilMain()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolderMain {
        val binding = ItemMainFragmentBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolderMain(binding)
    }

    override fun onBindViewHolder(holder: ViewHolderMain, position: Int) {
        holder.populate(currentList[holder.absoluteAdapterPosition], onChannelClick)
    }

    class ViewHolderMain(private val binding: ItemMainFragmentBinding): RecyclerView.ViewHolder(binding.root){
        fun populate(positionVideo: YTMainFragmentData, itemClickListener: OnClickYTListener){
            binding.openMore.setOnClickListener {
                itemClickListener.onClickOpenMore(positionVideo.query, positionVideo.title)
            }

            binding.textTitle.text = positionVideo.title

            val childAdapter = YTAdapterChild(itemClickListener)
            childAdapter.submitList(positionVideo.listResult)


            binding.childRW.apply {
                adapter = childAdapter
                onFlingListener = null
                addItemDecoration(DecoratorChildChannels(childAdapter.currentList.size))
            }
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding.childRW)

        }

    }
}
