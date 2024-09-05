package com.example.draupadi;

public class Contact {
    private String id;
    private String name;
    private String phone;
    private boolean isSos;

    public Contact() {
        // Default constructor required for calls to DataSnapshot.getValue(Contact.class)
    }

    public Contact(String id, String name, String phone, boolean isSos) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.isSos = isSos;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isSos() {
        return isSos;
    }

    public void setSos(boolean sos) {
        isSos = sos;
    }
}
