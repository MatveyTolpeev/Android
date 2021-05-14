package com.matvey.newsapp.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.matvey.newsapp.R
import com.matvey.newsapp.model.database.News

@Suppress("UNCHECKED_CAST")
class NewsListAdapter(private val loader: (url: String, view: ImageView) -> Unit, private var itemClickListener: OnItemClickListener)
    : ListAdapter<News, NewsListAdapter.NewsViewHolder>(ContactComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder.create(parent, loader)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, itemClickListener)
    }

    class NewsViewHolder(itemView: View, private val loader: (url: String, view: ImageView) -> Unit) : RecyclerView.ViewHolder(itemView) {
        private val newAuthorItemView: TextView = itemView.findViewById(R.id.new_author)
        private val newTitleItemView: TextView = itemView.findViewById(R.id.new_title)
        private val newImageItemView: ImageView = itemView.findViewById(R.id.new_image)


        fun bind(new: News?,
                 itemClickListener: OnItemClickListener) {
            newAuthorItemView.text = new?.author
            newTitleItemView.text = new?.title


            new?.let { new ->
                loader(new.urlToImage, newImageItemView)
                itemView.setOnClickListener{ itemClickListener.onItemClicked(new) }
            }
        }

        companion object {
            fun create(parent: ViewGroup, loader: (url: String, view: ImageView) -> Unit): NewsViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.recyclerview_item, parent, false)
                return NewsViewHolder(view, loader)
            }
        }
    }

    class ContactComparator : DiffUtil.ItemCallback<News>() {
        override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
            return oldItem.author == newItem.author
                    && oldItem.title == newItem.title

        }
    }

    companion object NewsListAdapterFactory {
        private var adapter: NewsListAdapter? = null
        fun getAdapter(loader: (url: String, view: ImageView) -> Unit, itemClickListener: OnItemClickListener) : NewsListAdapter {
            if (adapter == null) {
                adapter = NewsListAdapter(loader, itemClickListener)
            }
            return (adapter as NewsListAdapter).apply {
                this.itemClickListener = itemClickListener
            }
        }
    }
}