package com.tcpgame.clientgame.module;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class ClientConnection {
    private final static int PORT = 8080;
    private static Boolean FINISH = false;
    private static Player player;
    private static Dictionary<String, Player> playersConnected = new java.util.Hashtable<>();

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {


            System.out.println("Connected to server on port " + PORT);
//            new Thread(() -> send(out,"Message")).start();

            // Start a thread to listen for messages
            new Thread(() -> {
                try {
                    String receivedMessage;
                    while (!Objects.equals(receivedMessage = in.readLine(), "STOP") || !FINISH) {
                        if(receivedMessage!=null){
                            Gson gson = new Gson();
                            PlayerAction action = gson.fromJson(receivedMessage, PlayerAction.class);
                            JsonMessageGenerator jsonMessageGenerator = new JsonMessageGenerator();


                            switch (action.getAction()) {
                                case "newPlayer":

                                    if(action.getPlayer().equals("me") && player!=null){
                                        player.setId(action.getIdPlayer());
                                    }else{
                                        playersConnected.put(action.getIdPlayer(), new Player(action.getIdPlayer(),action.getPlayer(),100,100));
                                    }
                                   System.out.println("New player connected: " + action.getPlayer());
                                    break;
                                case "disconnect":
                                    send(out,jsonMessageGenerator.disconnect(player));
                                    send(out,"coucou");
                                    break;
                                case "CanMove":
//                                    send(out,jsonMessageGenerator.canMove(player,1,1));
                                    break;
                                case "canAttack":
                                    break;
                                default:
                                    System.out.println("Commande inconnue: " + receivedMessage);
                            }

                        }
                    }
                    if(receivedMessage.equals("STOP")){
                        System.out.println("Client stopped by the server.");
                    }else {
                        System.out.println("Disconnection done!");
                    }

                    FINISH = true;

                } catch (Exception e) {
                    System.err.println("Error in receiving messages: " + e.getMessage());
                }
            }).start();

            new Thread(() -> {
                Scanner scanner = new Scanner(System.in);
                String input;
                System.out.println("Saisissez une commande ('STOP' pour quitter) :");

                JsonMessageGenerator jsonMessageGenerator = new JsonMessageGenerator();
                while (!(input = scanner.nextLine()).equalsIgnoreCase("STOP")) {
                    System.out.println("Vous avez écrit : " + input);
                    // Utiliser println pour envoyer la commande avec un saut de ligne
//                    out.println(input);
                    player = new Player("test",100,100);

                    switch (input) {
                        case "connect":

                            String name;
                            System.out.println("Votre nom est: ");
                            name = scanner.nextLine();
                            player.setName(name);

                            send(out,jsonMessageGenerator.connect(player));
                            break;
                        case "disconnect":
                            send(out,jsonMessageGenerator.disconnect(player));
                            send(out,"coucou");
                            break;
                        case "CanMove":
//                            send(out,jsonMessageGenerator.canMove(player,1,1));
                            break;
                        case "canAttack":
                            break;
                        case "showPlayer":
                            if(!playersConnected.isEmpty()) {
                                for (Player playerShow : Collections.list(playersConnected.elements())) {
                                    System.out.println(playerShow.getName() + " : " + playerShow.getHealth());
                                }
                            }
                            System.out.println("Nombre de joueurs connéctés: " + playersConnected.size() + "");
                            break;
                        default:
                            System.out.println("Commande inconnue");
                    }

                    // On vérifie si le socket est fermé
                    if(socket.isClosed()){
                        System.out.println("Client arrêté par le serveur.");
                        break;
                    }
                }
                // Ici, vous pouvez définir une variable globale pour indiquer que le thread est terminé
//                FINISH = true;
            }).start();


            while (!FINISH) {

            }

        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }
    
    public static void send(PrintWriter out,String message) {

        out.println(message);
        System.out.println("Sending: " + message);
    }

}
