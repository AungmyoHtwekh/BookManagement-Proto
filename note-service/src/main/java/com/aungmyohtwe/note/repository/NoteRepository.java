package com.aungmyohtwe.note.repository;

import com.aungmyohtwe.note.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @Query(value = "SELECT * FROM note WHERE title LIKE %:keyword% OR message like %:keyword%", nativeQuery = true)
    Optional<List<Note>> findByKeyword(@Param("keyword") String keyword);
}
