package com.example.todolist.ui.mainScreen

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL
import android.widget.CheckBox
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
import com.example.todolist.util.showToast
import com.google.android.material.button.MaterialButton


class MainScreenFragment : Fragment() {

    private lateinit var binding: FragmentMainScreenBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MainScreenAdapter
    private var filterString = "title"
    private var mIsScrolling = false

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
            addNewTodo()
        }

    }

    private fun addNewTodo() {
        val bundle = Bundle()
        bundle.putBoolean("NewTodo", true)
        findNavController().navigate(
            R.id.action_mainScreenFragment_to_contentEditFragment,
            bundle
        )
    }

    private fun initRecyclerView() {
        recyclerView = binding.mainFragmentRecyclerView
        adapter = MainScreenAdapter(todoViewModel)

        val callback = TodoItemTouchHelper(adapter)
        val itemTouchHelper = ItemTouchHelper(callback)
        adapter.setTouchHelper(itemTouchHelper)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        recyclerView.adapter = adapter
        var mLayoutManager = LinearLayoutManager(APP_ACTIVITY)
        recyclerView.layoutManager = mLayoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_TOUCH_SCROLL) {
                    mIsScrolling = true
                    binding.mainScreenButtonAdd.hide()
                    if (mLayoutManager.findLastVisibleItemPosition() < 3) binding.mainScreenButtonAdd.show()
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (mIsScrolling
                    && dy < 0
                    && mLayoutManager.findFirstVisibleItemPosition() <= 3
                ) {
                    binding.mainScreenButtonAdd.show()
                }
            }

        })

        if (mLayoutManager.findLastVisibleItemPosition() < 3) binding.mainScreenButtonAdd.show()

        todoViewModel.allTodo.observe(APP_ACTIVITY, {
            adapter.addItem(it.toMutableList())
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        menu.findItem(R.id.menu_filter).setOnMenuItemClickListener {
            showDialog()
            false
        }
        val searchViewItem = menu?.findItem(R.id.app_bar_search)
        val searchView = searchViewItem?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                APP_ACTIVITY.actionBar?.title = ""
                todoViewModel.allTodo.observe(APP_ACTIVITY, {
                    val list = mutableListOf<Todo>()
                    it.forEach { item ->
                        if (filterString.toLowerCase().contains("title")) {
                            if (item.title.toLowerCase().contains(newText.toLowerCase())) {
                                if (!list.contains(item)) {
                                    list.add(item)
                                }
                            }
                        }
                        if (filterString.toLowerCase().contains("content")) {
                            if (item.content.toLowerCase().contains(newText.toLowerCase())) {
                                if (!list.contains(item)) {
                                    list.add(item)
                                }
                            }
                        }
                        if (filterString.toLowerCase().contains("status")) {
                            if (item.status.toLowerCase().contains(newText.toLowerCase())) {
                                if (!list.contains(item)) {
                                    list.add(item)
                                }
                            }
                        }
                        if (filterString.toLowerCase().contains("priority")) {
                            if (item.priority.toLowerCase().contains(newText.toLowerCase())) {
                                if (!list.contains(item)) {
                                    list.add(item)
                                }
                            }
                        }

                    }
                    adapter.addItem(list)
                    adapter.notifyDataSetChanged()
                })
                return false
            }

        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showDialog() {
        val dialog = Dialog(APP_ACTIVITY)
        dialog.setContentView(R.layout.dialog_filter)
        dialog.window?.setBackgroundDrawableResource(R.drawable.dr_item_recycler)
        val buttonSave = dialog.findViewById<MaterialButton>(R.id.dialog_filter_button_save)
        val checkBoxTitle = dialog.findViewById<CheckBox>(R.id.dialog_filter_checkbox_title)
        val checkBoxContent = dialog.findViewById<CheckBox>(R.id.dialog_filter_checkbox_content)
        val checkBoxStatus = dialog.findViewById<CheckBox>(R.id.dialog_filter_checkbox_status)
        val checkBoxPriority = dialog.findViewById<CheckBox>(R.id.dialog_filter_checkbox_priority)

        dialog.show()
        buttonSave.setOnClickListener {
            filterString = ""
            if (checkBoxTitle.isChecked) filterString += APP_ACTIVITY.getString(R.string.filter_title)
            if (checkBoxContent.isChecked) filterString += APP_ACTIVITY.getString(R.string.filter_content)
            if (checkBoxStatus.isChecked) filterString += APP_ACTIVITY.getString(R.string.filter_status)
            if (checkBoxPriority.isChecked) filterString += APP_ACTIVITY.getString(R.string.filter_priority)
            if (filterString == "") filterString = "title"
            showToast(filterString)
            dialog.hide()
        }
    }

    override fun onStop() {
        super.onStop()
        todoViewModel.allTodo.removeObservers(APP_ACTIVITY)
    }

}