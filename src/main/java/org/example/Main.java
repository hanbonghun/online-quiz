package org.example;

public class Main {

    public static void main(String[] args) {
        int port = 8070;
        new GameServer(port).start();
    }
}