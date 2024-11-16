package com.example.hihi;

public class Fruits {
    private String _id;
    private String name;
    private String color;
    private String taste;

    public Fruits() {
    }

    public Fruits(String _id, String name, String color, String taste) {
        this._id = _id;
        this.name = name;
        this.color = color;
        this.taste = taste;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }
}
