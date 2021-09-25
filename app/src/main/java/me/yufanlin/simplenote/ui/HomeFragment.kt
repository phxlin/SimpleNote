package me.yufanlin.simplenote.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import me.yufanlin.simplenote.databinding.FragmentHomeBinding
import me.yufanlin.simplenote.room.NoteDatabase

class HomeFragment : BaseFragment() {

    private lateinit var notesRVBinding: RecyclerView
    private var _binding: FragmentHomeBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        notesRVBinding = binding.notesRV

        notesRVBinding.apply {
            layoutManager = LinearLayoutManager(context)
        }

        binding.buttonAdd.setOnClickListener {
            val action = HomeFragmentDirections.actionAddNote()
            findNavController().navigate(action)
        }

        launch {
            context?.let {
                notesRVBinding.adapter = NotesAdapter(NoteDatabase(it).getNodeDAO().getAllNotes())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}