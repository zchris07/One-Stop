package com.controller;

import com.domain.Account;
import com.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("/findAll")
    public String findAll(Model model){

        List<Account> list=accountService.findAll();
        model.addAttribute("list", list);
        return "list";
    }
    @RequestMapping("/save")
    public void save(Account account, HttpServletResponse response, HttpServletRequest request){
        accountService.saveAccount(account);
        try {
            response.sendRedirect(request.getContextPath()+"/account/findAll");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ;
    }
    @RequestMapping("/add")
    public String add(Account account){
        accountService.addAccount(account);
        return "list";
    }
    @RequestMapping("/test")
    public void test(){
        accountService.test();
    }

}
