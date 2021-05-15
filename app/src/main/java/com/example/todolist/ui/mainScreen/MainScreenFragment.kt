package com.example.todolist.ui.mainScreen

import android.os.Bundle
import android.view.*
import android.widget.SearchView
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
import com.example.todolist.bd.Todo
import com.example.todolist.databinding.FragmentMainScreenBinding
import com.example.todolist.util.APP_ACTIVITY
import com.example.todolist.util.TodoItemTouchHelper
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
        setHasOptionsMenu(true)
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

        val callback = TodoItemTouchHelper(adapter)
        val itemTouchHelper = ItemTouchHelper(callback)
        adapter.setTouchHelper(itemTouchHelper)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(APP_ACTIVITY)
        todoViewModel.allTodo.observe(APP_ACTIVITY, {
            adapter.addItem(it.toMutableList())
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        val searchViewItem = menu?.findItem(R.id.app_bar_search)
        val searchView = searchViewItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                todoViewModel.allTodo.observe(APP_ACTIVITY, {
                    val list = mutableListOf<Todo>()
                    it.forEach { item ->
                        if (item.title.contains(newText)) {
                            list.add(item)
                        }
                    }
                    adapter.addItem(list)
                })
                return false
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }

}