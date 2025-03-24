package com.tcpgame.clientgame.module;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;
import java.util.*;

public class ClientConnection {
    private final static int PORT = 8080;
    private static volatile boolean FINISH = false;
    private static Player player = null;
    private static Dictionary<String, Player> playersConnected = new Hashtable<>();
    private static Thread listeningThread;
    private static Thread userInputThread;
    private static final int SIZEMAP = 10;
    private static String[][] map = new String[SIZEMAP][SIZEMAP];

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            System.out.println("Connected to server on port " + PORT);

            startListeningThread(in, socket);
            startUserInputThread(out, socket);

            while (!FINISH) {
                Thread.onSpinWait();
            }

            System.out.println("Client stopped.");
        } catch (Exception e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    private static void startListeningThread(BufferedReader in, Socket socket) {
        listeningThread = new Thread(() -> {
            try {
                String receivedMessage = "";
                while (!FINISH && !Objects.equals(receivedMessage = in.readLine(), "STOP")) {
                    if (receivedMessage != null) {
                        handleServerMessage(receivedMessage);
                    }
                    if (socket.isClosed()) {
                        System.out.println("Client arrêté par le serveur.");
                        stopThreads();
                        break;
                    }
                }
                if ("STOP".equals(receivedMessage)) {
                    System.out.println("Client stopped by the server.");
                } else {
                    System.out.println("Disconnection done!");
                }
                stopThreads();
            } catch (Exception e) {
                System.err.println("Error in receiving messages: " + e.getMessage());
            }
        });
        listeningThread.start();
    }

    private static void startUserInputThread(PrintWriter out, Socket socket) {
        userInputThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String input;
            System.out.println("Saisissez une commande ('STOP' pour quitter) :");
            JsonMessageGenerator jsonMessageGenerator = new JsonMessageGenerator();

            while (!FINISH && !(input = scanner.nextLine()).equalsIgnoreCase("STOP")) {
                processUserCommand(input, out, scanner, socket, jsonMessageGenerator);
            }
            stopThreads();
        });
        userInputThread.start();
    }

    private static void handleServerMessage(String receivedMessage) {
        Gson gson = new Gson();
        System.out.println(receivedMessage);
        PlayerAction action = gson.fromJson(receivedMessage, PlayerAction.class);

        switch (action.getAction()) {
            case "newPlayer":
                if ("me".equals(action.getName()) && player != null) {
                    System.out.println("my ID: " + action.getIdPlayer());
                    player.setId(action.getIdPlayer());

                    player.Move(new int[]{action.getX(), action.getY()});
                    map[action.getX()][action.getY()] = player.getId();
                } else {
                    System.out.println("Player add Id: " + receivedMessage);
                    playersConnected.put(action.getIdPlayer(), new Player(action.getIdPlayer(), action.getName(), 100, 100));
                    playersConnected.get(action.getIdPlayer()).Move(new int[]{action.getX(), action.getY()});
                    map[action.getX()][action.getY()] = action.getIdPlayer();

                    System.out.println("New player connected: " + action.getIdPlayer());
                }
                break;
            case "deletePlayer":
                if (playersConnected.get(action.getIdPlayer()) != null) {
                    map[playersConnected.get(action.getIdPlayer()).getPosition()[0]][playersConnected.get(action.getIdPlayer()).getPosition()[1]] = null;
                    playersConnected.remove(action.getIdPlayer());
                    System.out.println("Player disconnected: " + action.getIdPlayer());
                } else {
                    System.out.println("Player does not exist: " + action.getIdPlayer());
                }
                break;
            case "movePlayer":
                if(action.getName().equals("Me")) {
                    map[player.getPosition()[0]][player.getPosition()[1]] = null;
                    movePlayer(action.getX(), action.getY());
                    map[action.getX()][action.getY()] = player.getId();
                    System.out.println(player.getName()+" your mouvement is valid and actif !");

                }
                else if (playersConnected.get(action.getIdPlayer()) != null) {
                    playersConnected.get(action.getIdPlayer()).Move(new int[]{action.getX(), action.getY()});
                    map[playersConnected.get(action.getIdPlayer()).getPosition()[0]][playersConnected.get(action.getIdPlayer()).getPosition()[1]] = null;
                    map[action.getX()][action.getY()] = action.getIdPlayer();
                    System.out.println("Player moved: " + action.getIdPlayer());
                } else {
                    System.out.println("Player does not exist: " + action.getIdPlayer());
                }
                break;
            default:
                System.out.println("Commande inconnue: " + receivedMessage);
        }
    }



    private static void processUserCommand(String input, PrintWriter out, Scanner scanner, Socket socket, JsonMessageGenerator jsonMessageGenerator) {
        switch (input) {
            case "connect":
                System.out.println("Votre nom est: ");
                String name = scanner.nextLine();
                player = new Player(name, 100, 100);
                send(out, jsonMessageGenerator.connect(player));
                break;
            case "disconnect":
                System.out.println(player.getId());
                send(out, jsonMessageGenerator.disconnect(player));
                Disconnect(socket);
                break;
            case "showPlayer":
                if (!playersConnected.isEmpty()) {
                    Collections.list(playersConnected.elements()).forEach(p ->
                            System.out.println(p.getName() + " : " + p.getHealth() + " : " + Arrays.toString(p.getPosition())));
                }
                System.out.println("Nombre de joueurs connectés: " + playersConnected.size());
                break;
            case "move":
                System.out.println("Vous êtes en position: " + Arrays.toString(player.getPosition()));
                System.out.println("Quelle direction? (UP,DOWN,LEFT,RIGHT)");
                String direction = scanner.nextLine();
                int[] position = player.getPosition();
                switch (direction) {
                    case "UP":
                        canMove(position[0], position[1] - 1, out, jsonMessageGenerator);
                        break;
                    case "DOWN":
                        if(position[0]+ 1 < 10 && map[position[0]- 1][position[1]] == null) {
                            canMove(position[0], position[1] + 1, out, jsonMessageGenerator);
                        }
                        break;
                    case "LEFT":
                        if(position[0]+ 1 < 10 && map[position[0]- 1][position[1]] == null) {
                            canMove(position[0] - 1, position[1], out, jsonMessageGenerator);
                        }
                        break;
                    case "RIGHT":
                        if(position[0]+ 1 < 10 && map[position[0]+ 1][position[1]] == null) {
                            canMove(position[0] + 1, position[1], out, jsonMessageGenerator);
                        }
                        break;
                    default:
                        System.out.println("Direction inconnue");
                }
                break;
            default:
                System.out.println("Commande inconnue");
        }
    }

    private static void send(PrintWriter out, String message) {
        out.println(message);
        System.out.println("Sending: " + message);
    }

    private static void Disconnect(Socket socket) {
        stopThreads();
        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Client arrêté par le serveur.");
            }
        }
    }

    private static void canMove(int x, int y,PrintWriter out, JsonMessageGenerator jsonMessageGenerator) {
        if (player != null) {
            send(out, jsonMessageGenerator.canMove(player, x, y));
        }
    }

    private static void movePlayer(int x, int y) {
        if (player != null) {
            player.Move(new int[]{x, y});
        }
    }

    private static void stopThreads() {
        FINISH = true;
        if (listeningThread != null && listeningThread.isAlive()) {
            listeningThread.interrupt();
        }
        if (userInputThread != null && userInputThread.isAlive()) {
            userInputThread.interrupt();
        }
        System.out.println("Threads stopped.");
    }
}
