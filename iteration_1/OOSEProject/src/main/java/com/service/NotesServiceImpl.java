package com.service;

import com.dao.NotesDao;
import com.domain.Account;
import com.domain.Notes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("\notesService")
public class NotesServiceImpl implements NotesService{

    @Autowired
    private NotesDao notesDao;
    public List<Notes> findAll() {
        //System.out.println("find all account");
        return notesDao.findAll();
    }

    @Override
    public void saveNotes(Notes notes){
        System.out.println("save" + " "+ notes);
        notesDao.saveNotes(notes);
    }

    @Override
    public void addNotes(Notes notes) {
        notesDao.addNotes(notes);
    }

    @Override
    public void test() {
        System.out.println("Test!");
    }
}
