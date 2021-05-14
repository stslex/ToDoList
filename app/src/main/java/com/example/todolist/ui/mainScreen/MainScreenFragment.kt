package com.example.todolist.ui.mainScreen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.TodoApplication
import com.example.todolist.TodoViewModel
import com.example.todolist.TodoViewModelFactory
import com.example.todolist.databinding.FragmentMainScreenBinding
import com.example.todolist.util.APP_ACTIVITY
import com.example.todolist.util.hideKeyboard

class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainScreenAdapter

    private val todoViewModel: TodoViewModel by viewModels {
        TodoViewModelFactory((APP_ACTIVITY.application as TodoApplication).repository)
    }

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
            val bundle = Bundle()
            bundle.putBoolean("NewTodo", true)
            findNavController().navigate(
                R.id.action_mainScreenFragment_to_contentEditFragment,
                bundle
            )
        }

    }

    private fun initRecyclerView() {
        recyclerView = binding.mainFragmentRecyclerView
        adapter = MainScreenAdapter(todoViewModel)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(APP_ACTIVITY)
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
        todoViewModel.allTodo.observe(APP_ACTIVITY, {
            adapter.addItem(it.toMutableList())
        })
    }

    private val itemTouchHelperCallback =
        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.absoluteAdapterPosition
                val observer = todoViewModel.allTodo.observe(APP_ACTIVITY, {
                    Log.i(
                        "MainScreen::absoluteAdapterPosition",
                        position.toString()
                    )
                    val todo = it[position]
                    todoViewModel.delete(todo)
                    adapter.addItem(it.toMutableList())
                })
                todoViewModel.allTodo.removeObserver(observer)
            }


        }
}