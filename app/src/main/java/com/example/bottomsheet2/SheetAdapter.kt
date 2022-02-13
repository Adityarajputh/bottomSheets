package com.example.bottomsheet2

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bottomsheet2.databinding.SheetItemBinding

class SheetAdapter(val itemList: ArrayList<Items>): RecyclerView.Adapter<SheetAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClicked(description: String)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SheetAdapter.ViewHolder {

        return ViewHolder(SheetItemBinding.inflate(LayoutInflater.from(parent.context),
            parent, false), mListener)
    }

    override fun onBindViewHolder(holder: SheetAdapter.ViewHolder, position: Int) {

        val item = itemList[position]
        holder.bindItem(item)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(val itemBinding: SheetItemBinding, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemBinding.descriptionText.setOnClickListener {
                listener.onItemClicked(itemBinding.descriptionText.text.toString())
            }
        }

        fun bindItem(items: Items) {
            itemBinding.descriptionText.text = items.description
            itemBinding.logo.setImageResource(items.images)
        }

    }
}