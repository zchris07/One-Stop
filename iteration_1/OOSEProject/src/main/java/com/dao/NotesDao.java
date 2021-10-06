package com.dao;

import com.domain.Account;
import com.domain.Notes;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface NotesDao {
    @Select("select * from notes_storage")
    public List<Notes> findAll();

    @Insert("insert into notes_storage (id, date_taken,notes_id,notes,extension) values(110,#{date_taken},#{notes_id},#{notes},#{extension})")
    public void saveNotes(Notes notes);

    @Insert("\"insert into notes_storage (id, date_taken,notes_id,notes,extension) values(110,#{date_taken},#{notes_id},#{notes},#{extension})")
    void addNotes(Notes notes);
}
