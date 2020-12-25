package com.example.airmonitorizer2;

public class User {
    public String fullname, email, phone, admin, diseases, parameters;
    public User(){}
    public User(String fullname, String email, String phone){
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.admin = "no";
        this.diseases = "";
        this.parameters = "";
    }
}
