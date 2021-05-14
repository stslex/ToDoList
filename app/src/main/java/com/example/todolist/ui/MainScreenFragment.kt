package com.example.todolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.FragmentMainScreenBinding
import com.example.todolist.util.APP_ACTIVITY
import com.example.todolist.util.hideKeyboard

class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainScreenAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        hideKeyboard()
        initRecyclerView()
        binding.mainScreenButtonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreenFragment_to_contentEditFragment)
        }

    }

    private fun initRecyclerView() {
        recyclerView = binding.mainFragmentRecyclerView
        adapter = MainScreenAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(APP_ACTIVITY)
    }

}