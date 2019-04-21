package com.example.newp1;

/***
 *
 * User class
 *
 * store diary info:
 * - user name
 * - user email
 * - user password
 *
 * contain constructor and getter setter
 */
public class user {

    private int id;
    private String name;
    private String email;
    private String password;

    /**
    * contain constructor and getter setter
    */
    public user() {
    }

    public user(int id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    /**
     * contain constructor and getter setter
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}