package com.tcpgame.clientgame.module;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class ClientConnection {
    private final static int PORT = 8080;
    private static Boolean FINISH = false;
    private static Player player;
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected to server on port " + PORT);
//            new Thread(() -> send(out,"Message")).start();

            // Start a thread to listen for messages
            new Thread(() -> {
                try {
//                    String receivedMessage;
//                    while (!Objects.equals(receivedMessage = in.readLine(), "STOP") || !FINISH) {
//                        if(receivedMessage!=null){
//                            System.out.println("Received: " + receivedMessage);
//                        }
//                    }
//                    if(receivedMessage.equals("STOP")){
//                        System.out.println("Client stopped by the server.");
//                    }else {
//                        System.out.println("Disconnection done!");
//                    }

                    while (!FINISH) {
                        String receivedMessage = in.readLine();
                        if(receivedMessage!=null && !receivedMessage.equals("STOP")){
                            System.out.println("Received: " + receivedMessage);
                        }
                    }

//                    FINISH = true;

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
                            send(out,jsonMessageGenerator.connect(player));
                            break;
                        case "disconnect":
                            send(out,jsonMessageGenerator.disconnect(player));
                            send(out,"coucou");
                            break;
                        case "CanMove":
                            send(out,jsonMessageGenerator.canMove(player,1,1));
                            break;
                        case "canAttack":
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
