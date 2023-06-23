package com.example.prmmp1poprawka

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prmmp1poprawka.databinding.ListItemBinding

class AnimalViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(animal: Animal) {
        binding.nameEdit.text = animal.name
        binding.latinName.text = animal.latName
        binding.image.setImageResource(animal.resId)
    }
}

class AnimalsAdapter : RecyclerView.Adapter<AnimalViewHolder>() {
    private val data = mutableListOf<Animal>()

    interface OnItemClickListener {
        fun onItemClick(animal: Animal)
    }

    private var itemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: ((Animal) -> Boolean)? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: (Animal) -> Boolean) {
        onItemLongClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnimalViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(data[position])
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.invoke(data[position]) ?: false
        }
    }

    fun replace(newData: List<Animal>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }
}