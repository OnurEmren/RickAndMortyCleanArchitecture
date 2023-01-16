package com.onuremren.breakingbadapp.view.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onuremren.breakingbadapp.R
import com.onuremren.breakingbadapp.core.util.downloadFromUrl
import com.onuremren.breakingbadapp.core.util.loadWithGlide
import com.onuremren.breakingbadapp.databinding.CharacterItemBinding
import com.onuremren.breakingbadapp.model.CharacterList

class HomeCharactersAdapter(
    private val onItemClicked: (com.onuremren.breakingbadapp.model.Character) -> Unit
) : ListAdapter<com.onuremren.breakingbadapp.model.Character, HomeCharactersAdapter.ViewHolder>(
    DiffCallback()
) {

    var listCharacters = emptyList<com.onuremren.breakingbadapp.model.Character>()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CharacterItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding) {
            onItemClicked(this.getItem(it))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)


        if (currentItem.status == "Alive"){
            holder.binding.liveChar.visibility = View.VISIBLE
            holder.binding.unknownChar.visibility = View.GONE
            holder.binding.deadChar.visibility = View.GONE
        } else if (currentItem.status == "unknown"){
            holder.binding.liveChar.visibility = View.GONE
            holder.binding.unknownChar.visibility = View.VISIBLE
            holder.binding.deadChar.visibility = View.GONE
        } else{
            holder.binding.liveChar.visibility = View.GONE
            holder.binding.unknownChar.visibility = View.GONE
            holder.binding.deadChar.visibility = View.VISIBLE
        }


        holder.binding.imageView.loadWithGlide(holder.binding.character?.image)
        holder.itemView.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(currentItem,currentItem.location,position)
            Navigation.findNavController(it).navigate(action)
        }

    }

    class ViewHolder(val binding: CharacterItemBinding, onItemClicked: (Int) -> Unit) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { onItemClicked(absoluteAdapterPosition) }
        }

        fun bind(model: com.onuremren.breakingbadapp.model.Character) {
            binding.character = model

        }
    }



    class DiffCallback : DiffUtil.ItemCallback<com.onuremren.breakingbadapp.model.Character>() {
        override fun areItemsTheSame(
            oldItem: com.onuremren.breakingbadapp.model.Character,
            newItem: com.onuremren.breakingbadapp.model.Character
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: com.onuremren.breakingbadapp.model.Character,
            newItem: com.onuremren.breakingbadapp.model.Character
        ) =
            oldItem == newItem
    }
}