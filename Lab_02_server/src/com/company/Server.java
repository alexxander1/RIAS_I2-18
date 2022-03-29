package com.company;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


public class Server {

    public static final int PORT = 1502;
    public static LinkedList<ServerSomthing> serverList = new LinkedList<>(); // список всех нитей - экземпляров
    // сервера, слушающих каждый своего клиента
    public static Story story; // история переписки
    public static Bufer bufer; // история переписки


    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        story = new Story();
        bufer = new Bufer();
        System.out.println("Server Started");

        Timer timer = new Timer();
        timer.schedule(new Task(), 0, 10000);

        try {
            while (true) {
                // Блокируется до возникновения нового соединения:
                Socket socket = server.accept();
                try {
                    serverList.add(new ServerSomthing(socket)); // добавить новое соединенние в список
                } catch (IOException e) {
                    // Если завершится неудачей, закрывается сокет,
                    // в противном случае, нить закроет его:
                    socket.close();
                }
            }
        } finally {
            server.close();
        }
    }

    private static class Task extends TimerTask
    {

        @Override
        public void run()
        {
            for (ServerSomthing vr : Server.serverList) {
                Server.bufer.printBufer(vr.out);
            }
            Server.bufer.clearBufer();
        }

    }
}
