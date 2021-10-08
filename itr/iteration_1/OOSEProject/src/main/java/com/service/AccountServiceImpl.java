package com.service;

import com.dao.Accountdao;
import com.domain.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service("/accountService")
public class AccountServiceImpl implements AccountService {

    @Autowired
    private Accountdao accountdao;
    @Override
    public List<Account> findAll() {
        //System.out.println("find all account");
        return accountdao.findAll();
    }

    @Override
    public void saveAccount(Account account){
        System.out.println("save" + " "+ account);
        accountdao.saveAccount(account);
    }

    @Override
    public void addAccount(Account account) {
        accountdao.addAccount(account);
    }

    @Override
    public void test() {
        System.out.println("test!");
    }
}
