package com.tcpgame.clientgame.module;

public class Player {
    private String name;
    private String id;
    private int health;
    private int score;
    private int[] position;

    public Player(String name, int health, int score) {
        this.name = name;
        this.health = health;
        this.score = score;
    }

    public Player(String id, String name, int health, int score) {
        this.name = name;
        this.id = id;
        this.health = health;
        this.score = score;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void Move(int[] position) {
        this.position = position;
    }

    public void Hit(int damage) {
        this.health -= damage;
    }

    public void AddScore(int score) {
        this.score += score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
