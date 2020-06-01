package com.pratclot.mp3player.fragments.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pratclot.mp3player.databinding.ListItemBinding
import com.pratclot.mp3player.viewmodels.Audio

class MyTrackRecyclerViewAdapter(
    val clickListener: AudioListener
) : ListAdapter<Audio, MyTrackRecyclerViewAdapter.ViewHolder>(
    DiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

//    override fun getItemCount(): Int = values.size

    class ViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Audio, clickListener: AudioListener) {
            binding.audio = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Audio>() {
    override fun areItemsTheSame(oldItem: Audio, newItem: Audio): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Audio, newItem: Audio): Boolean {
        return oldItem.contentUri == newItem.contentUri
    }
}

class AudioListener(val clickListener: (contentUri: String) -> Unit) {
    fun onClick(audio: Audio) = clickListener(audio.contentUri)
}
