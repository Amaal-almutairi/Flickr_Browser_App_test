package com.example.flickr_browser_app_test

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.flickr_browser_app_test.databinding.ItemRowBinding


class RVAdap(val Activity:MainActivity, val IMAGES:ArrayList<imge>):RecyclerView.Adapter<RVAdap.ItemViewHolder>(){
    class ItemViewHolder( val binding:ItemRowBinding):RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ItemRowBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val images = IMAGES[position]
        holder.binding.apply {
            tvimg.text=images.title
            Glide.with(Activity).load(images.link).into(imgrv)
            lineid.setOnClickListener { Activity.openItem(images.link) }

        }

    }

    override fun getItemCount(): Int = (IMAGES.size)

}





