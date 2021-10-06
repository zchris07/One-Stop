package com.domain;

import java.io.Serializable;



public class Account implements Serializable {
    private Integer id;
    private String username;
    private String registerDate;
    private String birthday;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username=" + username + '\'' +
                ", registerDate=" + registerDate +
                ", birthday=" + birthday +
                '}';
    }
}
