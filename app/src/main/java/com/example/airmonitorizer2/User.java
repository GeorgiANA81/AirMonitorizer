package com.example.airmonitorizer2;

public class User {
    public String fullname, email, phone, admin, diseases;
    public User(){}
    public User(String fullname, String email, String phone){
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.admin = "no";
        this.diseases = "";
    }
    public String getFullname(){
        return fullname;
    }
    public String getEmail(){
        return email;
    }
    public String getPhone(){
        return phone;
    }
    public String getAdmin(){
        return admin;
    }
}
