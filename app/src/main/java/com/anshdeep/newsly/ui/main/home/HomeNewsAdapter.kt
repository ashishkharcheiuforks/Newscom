package com.anshdeep.newsly.ui.main.home

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.anshdeep.newsly.R
import com.anshdeep.newsly.databinding.RvItemNewsBinding
import com.anshdeep.newsly.model.Articles
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by ansh on 13/02/18.
 */
class HomeNewsAdapter(private var items: List<Articles>,
                      private var listener: OnItemClickListener)
    : RecyclerView.Adapter<HomeNewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = RvItemNewsBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(items[position], listener)
    }

    override fun getItemCount(): Int = items.size

    interface OnItemClickListener {
        fun onItemClick(article: Articles)
    }

    fun replaceData(arrayList: List<Articles>) {
        items = arrayList
        notifyDataSetChanged()
    }


    fun convertPublishedTime(publishTime: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("IST")
        val time = sdf.parse(publishTime).time
        val now = System.currentTimeMillis()

        val ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS)
        return ago.toString()
    }

    // ViewHolder takes instance of RvItemNewsBinding type instead of View type so
    // we can implement Data Binding in ViewHolder for each item
    inner class ViewHolder(private var binding: RvItemNewsBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(news: Articles, listener: OnItemClickListener?) {
            binding.news = news

            val requestOptions = RequestOptions().error(R.drawable.noimg)
            Glide.with(binding.newsThumbnail)
                    .load(news.urlToImage)
                    .apply(requestOptions)
                    .transition(withCrossFade())
                    .into(binding.newsThumbnail)

            val newTime = convertPublishedTime(news.publishedAt)
            binding.newsTime.text = newTime
            if (listener != null) {
                binding.root.setOnClickListener { listener.onItemClick(news) }
            }

            binding.executePendingBindings()
        }
    }
}