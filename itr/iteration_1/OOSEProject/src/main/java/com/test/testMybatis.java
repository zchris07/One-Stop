package com.test;

import com.dao.Accountdao;
import com.domain.Account;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class testMybatis {
//    @Test
//    public void run1() throws Exception {
//        InputStream in = Resources.getResourceAsStream("SqlConfig.xml");
//
//        SqlSessionFactory factory=new SqlSessionFactoryBuilder().build(in);
//        SqlSession session=factory.openSession();
//        Accountdao dao=session.getMapper(Accountdao.class);
//        List<Account> list=dao.findAll();
//        for (Account account:list) {
//            System.out.println(account);
//
//        }
//        session.close();
//        in.close();
//    }
//    @Test
//    public void run2() throws Exception {
//        Account account = new Account();
//        account.setName("John");
//        account.setMoney(200.0);
//        InputStream in = Resources.getResourceAsStream("SqlConfig.xml");
//
//        SqlSessionFactory factory=new SqlSessionFactoryBuilder().build(in);
//        SqlSession session=factory.openSession();
//        Accountdao dao=session.getMapper(Accountdao.class);
//        dao.saveAccount(account);
//        session.commit();
//
//
//        session.close();
//        in.close();
//    }
}
