package me.yufanlin.simplenote.ui

import android.view.View
import me.yufanlin.simplenote.room.Note

interface NoteClickListener {
    fun onNoteClicked(view: View, note: Note)
}