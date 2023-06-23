package com.example.mp3test.adapters

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mp3test.data.DataSource
import com.example.mp3test.models.Article
import com.example.mp3test.databinding.ListItemBinding

class ArticleViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(article: Article) {
        Glide.with(binding.image)
            .load(article.imageUrl)
            .into(binding.image)
        binding.name.text = article.title

        val isSelected = DataSource.loggedInUser?.articleTitles?.contains(article.title) == true
        if (isSelected) {
            binding.root.setBackgroundColor(Color.GRAY)
        } else {
            binding.root.setBackgroundColor(Color.TRANSPARENT)
        }
    }
}

class ArticleAdapter : RecyclerView.Adapter<ArticleViewHolder>() {
    private val data = mutableListOf<Article>()

    interface OnItemClickListener {
        fun onItemClick(article: Article)
    }

    private var itemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ArticleViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = data[position]
        holder.bind(article)
        holder.itemView.setOnClickListener {
            itemClickListener?.onItemClick(article)
        }
    }

    fun replace(newData: List<Article>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

}