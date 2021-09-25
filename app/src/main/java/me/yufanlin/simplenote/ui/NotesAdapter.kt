package me.yufanlin.simplenote.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import me.yufanlin.simplenote.databinding.ItemNoteBinding
import me.yufanlin.simplenote.room.Note

class NotesAdapter(var notes: List<Note>) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NoteViewHolder(
            ItemNoteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount() = notes.size

    class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root),
        NoteClickListener {

        override fun onNoteClicked(view: View, note: Note) {
            val action = HomeFragmentDirections.actionAddNote()
            action.note = note
            Navigation.findNavController(view).navigate(action)
        }

        fun bind(note: Note) {
            binding.note = note
            binding.listener = this
        }
    }
}
