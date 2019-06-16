package com.palopodstreleny.facenet.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.palopodstreleny.facenet.databinding.LayoutUserEmbedingsBinding
import com.palopodstreleny.facenet.db.UserEmbedding

/**
 *
 * RecyclerView adapter for UserEmbeddings
 *
 */
class UserEmbeddingsAdapter : RecyclerView.Adapter<UserEmbeddingsAdapter.UserEmbeddingViewHolder>() {

    private var data = ArrayList<UserEmbedding>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserEmbeddingViewHolder {

        val binding = DataBindingUtil.inflate<LayoutUserEmbedingsBinding>(
            LayoutInflater.from(parent.context),
            com.palopodstreleny.facenet.R.layout.layout_user_embedings, parent, false
        )

        return UserEmbeddingViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: UserEmbeddingViewHolder, position: Int) {
        holder.binding.userBindingTv.text = data[position].toString()
    }

    /**
     *
     * Swap old data with new one
     *
     * @param data ArrayList<UserEmbedding>
     *
     */
    fun add(data: ArrayList<UserEmbedding>){
        this.data = data
        notifyDataSetChanged()
    }


    /**
     *
     * ViewHolder for UserEmbeddings
     *
     * @param binding data binding
     *
     */
    class UserEmbeddingViewHolder(val binding: LayoutUserEmbedingsBinding) : RecyclerView.ViewHolder(binding.root)


}