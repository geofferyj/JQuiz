package com.geofferyj.jquiz.ui.fragments.courses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.geofferyj.jquiz.databinding.CourseItemBinding
import com.geofferyj.jquiz.models.Course

class CourseRVAdapter() :
    RecyclerView.Adapter<CourseRVAdapter.CourseItemVH>() {

    private val differCallback = object : DiffUtil.ItemCallback<Course>() {
        override fun areItemsTheSame(
            oldItem: Course,
            newItem: Course
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Course,
            newItem: Course
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    private var onItemClickListener: ((position: Int, item: Course) -> Unit)? = null

    fun setOnItemClickListener(listener: (position: Int, item: Course) -> Unit) {
        onItemClickListener = listener
    }


    inner class CourseItemVH(private val binding: CourseItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Course) {

            binding.course = item
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(absoluteAdapterPosition, item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseItemVH {
        val binding =
            CourseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CourseItemVH(binding)

    }

    override fun onBindViewHolder(holder: CourseItemVH, position: Int) {
        val item = differ.currentList[position]
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}