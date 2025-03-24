package com.tcpgame.clientgame.module;

import com.google.gson.Gson;

public class JsonMessageGenerator {
    private Gson gson = new Gson();

    public String connect(Player player) {
        PlayerAction msg = new PlayerAction("connect",  player.getId(), player.getName(),null, null );
        return gson.toJson(msg);
    }

    public String disconnect(Player player) {
        PlayerAction msg = new PlayerAction("disconnect", player.getId(), player.getId(), null, null);
        return gson.toJson(msg);
    }
//
    public String canMove(Player player, int x, int y) {
        PlayerAction msg = new PlayerAction("CanMove",  player.getId(), player.getName(), x, y);
        return gson.toJson(msg);
    }
//
//    public String canAttack(Player player, String idPlayerCible) {
//        PlayerAction msg = new PlayerAction("CanAttack", String.valueOf(player.getId()), null, null, null, null);
//        return gson.toJson(msg);
//    }
//
    public String isMyTurn(Player player) {
        PlayerAction msg = new PlayerAction("isMyTurn", player.getId());
        return gson.toJson(msg);
    }
}
