package com.sortstring.firsttask;

public class SignUpModel {
    public String uid;
    public String email;
    public SignUpModel() { }

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public SignUpModel(String uid, String email) {
        this.uid = uid;
        this.email = email;

    }
}
