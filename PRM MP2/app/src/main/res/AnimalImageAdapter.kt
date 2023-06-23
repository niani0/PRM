package com.example.prmmp1poprawka

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prmmp1poprawka.databinding.AnimalImageBinding

class AnimalImageViewHolder(val binding: AnimalImageBinding) :
    RecyclerView.ViewHolder(binding.root) {


    fun bind(resId: Int, isSelected: Boolean) {
        binding.image.setImageResource(resId)
        binding.selectedFrame.visibility =
            if (isSelected) View.VISIBLE else View.INVISIBLE
    }
}

class AnimalImageAdapter : RecyclerView.Adapter<AnimalImageViewHolder>() {
    private val images = listOf(
        R.drawable.monke,
        R.drawable.moose,
        R.drawable.sheep,
        R.drawable.tiger,
        R.drawable.parrot,
        R.drawable.pingu
    )
    private var selectedPosition: Int = 0
    val selectedIdRes: Int
        get() = images[selectedPosition]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalImageViewHolder {
        val binding = AnimalImageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AnimalImageViewHolder(binding).also { vh ->
            binding.root.setOnClickListener {
                notifyItemChanged(selectedPosition)
                selectedPosition = vh.layoutPosition
                notifyItemChanged(selectedPosition)
            }
        }
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: AnimalImageViewHolder, position: Int) {
        holder.bind(images[position], position == selectedPosition)
    }
}