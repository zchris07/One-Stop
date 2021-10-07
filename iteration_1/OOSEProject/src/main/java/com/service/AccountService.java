package com.service;

import com.domain.Account;

import java.util.List;

public interface AccountService {
    public List<Account> findAll();
    public void saveAccount(Account account);

    void addAccount(Account account);

    void test();
}
