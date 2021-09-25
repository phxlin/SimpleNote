package me.yufanlin.simplenote.ui

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import me.yufanlin.simplenote.R
import me.yufanlin.simplenote.databinding.FragmentAddNoteBinding
import me.yufanlin.simplenote.room.Note
import me.yufanlin.simplenote.room.NoteDatabase

class AddNoteFragment : BaseFragment() {

    private var note: Note? = null

    private var _binding: FragmentAddNoteBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            note = AddNoteFragmentArgs.fromBundle(it).note
            binding.titleET.setText(note?.title)
            binding.bodyET.setText(note?.body)
        }

        binding.buttonSave.setOnClickListener {
            val noteTitle = binding.titleET.text.toString().trim()
            val noteBody = binding.bodyET.text.toString().trim()

            if (noteTitle.isEmpty()) {
                binding.titleET.error = "Title required"
                return@setOnClickListener
            }

            if (noteBody.isEmpty()) {
                binding.bodyET.error = "Body required"
                return@setOnClickListener
            }

            val newNote = Note(noteTitle, noteBody)

            launch {
                if (note == null) saveNote(newNote) else updateNote(newNote)
            }

            navigateBack()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.note_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.deleteNote -> {
                note?.let {
                    deleteNote()
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private suspend fun updateNote(newNote: Note) {
        newNote.id = note!!.id
        context?.let {
            NoteDatabase(it).getNodeDAO().updateNote(newNote)
            Toast.makeText(it, "Note updated", Toast.LENGTH_SHORT).show()
        }
    }

    private suspend fun saveNote(note: Note) {
        context?.let {
            NoteDatabase(it).getNodeDAO().addNote(note)
            Toast.makeText(it, "Note created", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteNote() {
        context?.let {
            AlertDialog.Builder(it).apply {
                setTitle("Are you sure?")
                setMessage("You cannot undo this operation.")
                setPositiveButton("Okay") { dialog: DialogInterface, which: Int ->
                    launch {
                        NoteDatabase(it).getNodeDAO().deleteNote(note!!)
                        navigateBack()
                    }
                }
                setNegativeButton("Cancel") { dialog: DialogInterface, which: Int -> }
                show()
            }
        }
    }

    private fun navigateBack() {
        val action = AddNoteFragmentDirections.actionSaveNote()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}