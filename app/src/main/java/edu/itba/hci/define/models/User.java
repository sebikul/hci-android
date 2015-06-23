package edu.itba.hci.define.models;


import java.io.Serializable;

public class User extends ApiResponse implements Serializable{

    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private char gender;

    public User() {
    }

    private String identityCard;
    private String email;
    private transient String authToken;

    private String birthDate;
    private String createdDate;
    private String lastLoginDate;
    private String lastPasswordChangeDate;

    public User(int id, String username, String firstName, String lastName, char gender, String identityCard, String email, String birthDate, String createdDate, String lastLoginDate, String lastPasswordChangeDate) {
        this.id = id;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.identityCard = identityCard;
        this.email = email;
        this.birthDate = birthDate;
        this.createdDate = createdDate;
        this.lastLoginDate = lastLoginDate;
        this.lastPasswordChangeDate = lastPasswordChangeDate;
    }

    public String getLastPasswordChangeDate() {
        return lastPasswordChangeDate;
    }

    public String getLastLoginDate() {
        return lastLoginDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public char getGender() {
        return gender;
    }

    public String getIdentityCard() {
        return identityCard;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender=" + gender +
                ", identityCard='" + identityCard + '\'' +
                ", email='" + email + '\'' +
                ", authToken='" + authToken + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", lastLoginDate='" + lastLoginDate + '\'' +
                ", lastPasswordChangeDate='" + lastPasswordChangeDate + '\'' +
                '}';
    }

    public String getEmail() {
        return email;
    }
}
