package com.example.todolist.ui

import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todolist.R
import com.example.todolist.databinding.FragmentContentEditBinding
import com.example.todolist.util.APP_ACTIVITY
import com.example.todolist.util.hideKeyboard

class ContentEditFragment : Fragment() {

    private lateinit var binding: FragmentContentEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContentEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        hideKeyboard()
        APP_ACTIVITY.actionBar?.setDisplayShowHomeEnabled(true)
        APP_ACTIVITY.actionBar?.setHomeButtonEnabled(true)
    }

}