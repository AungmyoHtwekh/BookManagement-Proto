package com.aungmyohtwe.note.service.impl;

import com.aungmyohtwe.note.model.Note;
import com.aungmyohtwe.note.repository.NoteRepository;
import com.aungmyohtwe.note.service.NoteService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {

    private final static Logger log = Logger.getLogger(NoteServiceImpl.class);

    @Autowired
    private NoteRepository noteRepository;

    @Override
    public List<Note> findByKeyword(String keyword) {
        Optional<List<Note>> noteList = noteRepository.findByKeyword(keyword);
        if (noteList.isPresent()){
            log.info("Getting note list results.");
            return noteList.get();
        }
        return null;
    }

    @Override
    public void saveNote(Note note) {
        noteRepository.saveAndFlush(note);
    }

    @Override
    public void deleteNote(Note note) {
        noteRepository.delete(note);
    }

    @Override
    public Note findById(Long id) {
        Optional<Note> note = noteRepository.findById(id);
        return note.get();
    }
}
