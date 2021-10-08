package com.controller;

import com.service.NotesService;
import com.domain.Account;
import com.domain.Notes;
import com.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author He Zhenyong
 */
@Controller
@RequestMapping("/note")
public class NotesController {
    @Autowired
    private NotesService notesService;

    @RequestMapping("/findAll")
    public String findAll(Model model){

        List<Notes> list=notesService.findAll();
        model.addAttribute("list", list);
        return "list";
    }
    @RequestMapping("/save")
    public void save(Notes notes, HttpServletResponse response, HttpServletRequest request){
        notesService.saveNotes(notes);
        try {
            response.sendRedirect(request.getContextPath()+"/notes/findAll");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ;
    }
    @RequestMapping("/add")
    public String add(Notes notes){
        notesService.addNotes(notes);
        return "list";
    }
    @RequestMapping("/test")
    public void test(){
        notesService.test();
    }

}
