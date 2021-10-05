package com.dao;

import com.domain.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface Accountdao {

    @Select("select * from account")
    public List<Account> findAll();

    @Insert("insert into userlocal (id, username,registerDate) values(110,#{username},#{registerDate})")
    public void saveAccount(Account account);

    @Insert("insert into account (name, money) values(#{name},#{money})")
    void addAccount(Account account);
}
