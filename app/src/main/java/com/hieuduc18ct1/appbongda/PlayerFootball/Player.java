package com.hieuduc18ct1.appbongda.PlayerFootball;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Player implements Serializable {
    private int id = 0;
    private Bitmap avatar;
    private String name = "";
    private String birth = "";
    private String pos = "";
    private String contry = "";
    private String values = "";
    private String trophy = "";
    private boolean like  = false;



    public Player(int id,boolean like) {
        this.id = id;
        this.like = like;
    }
    public Player(Bitmap avatar) {
        this.avatar = avatar;
    }

    public Player(int id, Bitmap avatar, String name, String birth, String pos, String contry, String values, String trophy, boolean like) {
        this.id = id;
        this.avatar = avatar;
        this.name = name;
        this.birth = birth;
        this.pos = pos;
        this.contry = contry;
        this.values = values;
        this.trophy = trophy;
        this.like = like;
    }

    public Player(Bitmap avatar, String name, String birth, String pos, String contry, String values, String trophy, boolean like) {
        this.avatar = avatar;
        this.name = name;
        this.birth = birth;
        this.pos = pos;
        this.contry = contry;
        this.values = values;
        this.trophy = trophy;
        this.like = like;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar(Bitmap avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getContry() {
        return contry;
    }

    public void setContry(String contry) {
        this.contry = contry;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getTrophy() {
        return trophy;
    }

    public void setTrophy(String trophy) {
        this.trophy = trophy;
    }

    public boolean isLike() {
        return like;
    }

    public void setLike(boolean like) {
        this.like = like;
    }
}
