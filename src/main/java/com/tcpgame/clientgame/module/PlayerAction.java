package com.tcpgame.clientgame.module;

public class PlayerAction {

//    public String connect(Player name){
//        return "{Action:connect,Player:"+name.getName()+"}";
//    }
//
//    public String disconnect(Player name){
//        return "{Action:disconnect,Player:"+name.getId()+"}";
//    }
//
//    public String CanMove(Player name ,int X, int Y){
//        return "{Action:CanMove,Player:"+name.getId()+",X:"+X+",Y:"+Y+"}";
//    }
//
//    public String CanAttack(Player name,String idPlayerCible){
//        return "{Action:CanAttack,Player:"+name.getId()+",idPlayer:"+idPlayerCible+"}";
//    }
//
//    public String isMyTurn(Player name){
//        return "{Action:isMyTurn,Player:"+name.getId()+"}";
//    }

    private String action;
    private String name;
    private Integer x;      // optionnel
    private Integer y;      // optionnel
    private String idPlayer; // optionnel pour l'attaque

    // Constructeurs, getters et setters


    public PlayerAction(String action, String idPlayer, String name, Integer x, Integer y) {
        this.action = action;
        this.name = name;
        this.x = x;
        this.y = y;
        this.idPlayer = idPlayer;
    }

    public PlayerAction(String action, String name) {
        this.action = action;
        this.name = name;
    }

    // Surcharge pour CanMove
    public PlayerAction(String action, String idPlayer, int x, int y) {
        this.action = action;
        this.idPlayer = idPlayer;
        this.x = x;
        this.y = y;
    }

    // Surcharge pour CanAttack
    public PlayerAction(String action, String name, String idPlayer) {
        this.action = action;
        this.name = name;
        this.idPlayer = idPlayer;
    }

    // Getters et setters...
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public String getIdPlayer() {
        return idPlayer;
    }

    public void setIdPlayer(String idPlayer) {
        this.idPlayer = idPlayer;
    }


}
