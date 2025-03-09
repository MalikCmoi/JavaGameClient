package com.tcpgame.clientgame.module;

public class PlayerAction {

//    public String connect(Player player){
//        return "{Action:connect,Player:"+player.getName()+"}";
//    }
//
//    public String disconnect(Player player){
//        return "{Action:disconnect,Player:"+player.getId()+"}";
//    }
//
//    public String CanMove(Player player ,int X, int Y){
//        return "{Action:CanMove,Player:"+player.getId()+",X:"+X+",Y:"+Y+"}";
//    }
//
//    public String CanAttack(Player player,String idPlayerCible){
//        return "{Action:CanAttack,Player:"+player.getId()+",idPlayer:"+idPlayerCible+"}";
//    }
//
//    public String isMyTurn(Player player){
//        return "{Action:isMyTurn,Player:"+player.getId()+"}";
//    }

    private String action;
    private String player;
    private Integer x;      // optionnel
    private Integer y;      // optionnel
    private String idPlayer; // optionnel pour l'attaque

    // Constructeurs, getters et setters

    public PlayerAction(String action, String player) {
        this.action = action;
        this.player = player;
    }

    // Surcharge pour CanMove
    public PlayerAction(String action, String player, int x, int y) {
        this.action = action;
        this.player = player;
        this.x = x;
        this.y = y;
    }

    // Surcharge pour CanAttack
    public PlayerAction(String action, String player, String idPlayer) {
        this.action = action;
        this.player = player;
        this.idPlayer = idPlayer;
    }

    // Getters et setters...
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
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
