package com.example.myapplication.adapters

import android.annotation.SuppressLint
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.model.ProductEntity
import com.example.myapplication.databinding.ListItemBinding

class ProductViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(product: ProductEntity) {
        binding.nameEdit.text = product.name
        binding.latinName.text = product.loc
        binding.image.setImageURI(Uri.parse(product.icon.toString()))
    }
}

class ProductAdapter : RecyclerView.Adapter<ProductViewHolder>() {
    private val data = mutableListOf<ProductEntity>()

    interface OnItemClickListener {
        fun onItemClick(product: ProductEntity)
    }

    private var itemClickListener: OnItemClickListener? = null
    private var onItemLongClickListener: ((ProductEntity) -> Boolean)? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun setOnItemLongClickListener(listener: (ProductEntity) -> Boolean) {
        onItemLongClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = data[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(product)
        }
        holder.itemView.setOnLongClickListener {
            onItemLongClickListener?.invoke(product) ?: false
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun replace(newData: List<ProductEntity>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

}