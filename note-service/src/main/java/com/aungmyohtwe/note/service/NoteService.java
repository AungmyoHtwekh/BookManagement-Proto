package com.aungmyohtwe.note.service;

import com.aungmyohtwe.note.model.Note;

import java.util.List;

public interface NoteService {
    List<Note> findByKeyword(String keyword);
    void saveNote(Note note);
    void deleteNote(Note note);
    Note findById(Long id);
}
