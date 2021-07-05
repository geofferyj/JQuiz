package com.geofferyj.jquiz.ui.fragments.questions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.geofferyj.jquiz.databinding.CourseItemBinding
import com.geofferyj.jquiz.databinding.QuestionItemBinding
import com.geofferyj.jquiz.models.Course
import com.geofferyj.jquiz.models.Question

class QuestionRVAdapter() :
    RecyclerView.Adapter<QuestionRVAdapter.QuestionItemVH>() {

    private val differCallback = object : DiffUtil.ItemCallback<Question>() {
        override fun areItemsTheSame(
            oldItem: Question,
            newItem: Question
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Question,
            newItem: Question
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((position: Int, item: Question) -> Unit)? = null

    fun setOnItemClickListener(listener: (position: Int, item: Question) -> Unit) {
        onItemClickListener = listener
    }


    inner class QuestionItemVH(private val binding: QuestionItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Question) {

            binding.question = item
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(absoluteAdapterPosition, item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionItemVH {
        val binding =
            QuestionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionItemVH(binding)

    }

    override fun onBindViewHolder(holder: QuestionItemVH, position: Int) {
        val item = differ.currentList[position]
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}