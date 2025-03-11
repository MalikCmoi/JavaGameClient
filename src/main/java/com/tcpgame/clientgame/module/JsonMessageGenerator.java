package com.tcpgame.clientgame.module;

import com.google.gson.Gson;

public class JsonMessageGenerator {
    private Gson gson = new Gson();

    public String connect(Player player) {
        PlayerAction msg = new PlayerAction("connect", player.getName(), null, null, null);
        return gson.toJson(msg);
    }

    public String disconnect(Player player) {
        PlayerAction msg = new PlayerAction("disconnect", String.valueOf(player.getId()), null, null, null);
        return gson.toJson(msg);
    }
//
//    public String canMove(Player player, int x, int y) {
//        PlayerAction msg = new PlayerAction("CanMove", String.valueOf(player.getId()),, null, null, null);
//        return gson.toJson(msg);
//    }
//
//    public String canAttack(Player player, String idPlayerCible) {
//        PlayerAction msg = new PlayerAction("CanAttack", String.valueOf(player.getId()), null, null, null, null);
//        return gson.toJson(msg);
//    }
//
//    public String isMyTurn(Player player) {
//        PlayerAction msg = new PlayerAction("isMyTurn", String.valueOf(player.getId()));
//        return gson.toJson(msg);
//    }
}
