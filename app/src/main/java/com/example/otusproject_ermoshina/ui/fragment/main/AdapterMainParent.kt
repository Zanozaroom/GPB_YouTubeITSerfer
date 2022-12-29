package com.example.otusproject_ermoshina.ui.fragment.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.otusproject_ermoshina.domain.model.YTSearchAndTitle
import com.example.otusproject_ermoshina.databinding.ItemMainFragmentBinding
import com.example.otusproject_ermoshina.utill.DecoratorChildChannels


class ExDiffUtilMain: DiffUtil.ItemCallback<YTSearchAndTitle>() {
    override fun areItemsTheSame(oldItem: YTSearchAndTitle, newItem: YTSearchAndTitle): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: YTSearchAndTitle, newItem: YTSearchAndTitle): Boolean = oldItem == newItem
}
class AdapterMainParent(private val onChannelClick: OnClickYTListener) : ListAdapter<YTSearchAndTitle, AdapterMainParent.ViewHolderMain>(
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
        fun populate(positionVideo: YTSearchAndTitle, itemClickListener: OnClickYTListener){
            binding.openMore.setOnClickListener {
                itemClickListener.onClickOpenMore(positionVideo.query)
            }

            binding.textTitle.text = positionVideo.title

            val childAdapter = YTAdapterChild(itemClickListener)
            childAdapter.submitList(positionVideo.listResult)


            binding.childRW.apply {
                adapter = childAdapter
                onFlingListener = null
                addItemDecoration(DecoratorChildChannels(childAdapter.currentList.size,context))
            }
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding.childRW)

        }

    }
}
