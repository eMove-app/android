package com.emove.emove.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.emove.emove.R
import com.emove.emove.model.SearchResult
import kotlinx.android.synthetic.main.search_result.view.*

class SearchResultsAdapter(val items: List<SearchResult>, val listener: SearchResultsListener) : RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    interface SearchResultsListener {
        fun onItemSelected(searchResult: SearchResult)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.search_result, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position], listener)

    override fun getItemCount() = items.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: SearchResult, listener: SearchResultsListener) = with(itemView) {
            tv_name.text = item.user.name
            Glide.with(this).load(item.user.profile_picture_url).into(iv_picture);
            tv_details.setOnClickListener { listener.onItemSelected(item) }
        }
    }
}