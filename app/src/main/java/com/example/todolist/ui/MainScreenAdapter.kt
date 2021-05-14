package com.example.todolist.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.MainScreenItemBinding

class MainScreenAdapter : RecyclerView.Adapter<MainScreenAdapter.MainViewHolder>() {

    class MainViewHolder(binding: MainScreenItemBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = MainScreenItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = 10

    fun addItem() {

    }
}