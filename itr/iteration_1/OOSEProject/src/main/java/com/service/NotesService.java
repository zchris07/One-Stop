package com.service;

import com.domain.Account;
import com.domain.Notes;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author He Zhenyong
 */
public interface NotesService {


    public List<Notes> findAll();

    public void saveNotes(Notes notes);
    public void addNotes(Notes notes);

    void test();
}
