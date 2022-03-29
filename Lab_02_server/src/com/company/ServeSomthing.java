package com.company;

import java.io.*;
import java.net.Socket;

//Поток ServerSomthing
class ServerSomthing extends Thread {

    private Socket socket; // сокет сервера
    private BufferedReader in;  // буфер чтения из сокета
    public BufferedWriter out; // буфер завписи в сокет


    public ServerSomthing(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        Server.story.printStory(out); // поток вывода передаётся для передачи истории последних 10 сообщений новому поключению
        start(); //старт связи в бесконечном цикле
    }
    @Override
    public void run() {
        String word;
        try {
            // первое сообщение отправленное сюда - это никнейм
            word = in.readLine();
            try {
                out.write(word + "\n");
                out.flush(); // чистка буфера
            } catch (IOException ignored) {}

            try {
                while (true) {
                    word = in.readLine();
                    if(word.equals("stop admin 12345")) {
                        this.stopService(); // стоп сервиса
                    }
                    System.out.println("Echoing: " + word);


                    Server.story.addStoryEl(word);  //Добовляем сообщение в историю
                    Server.bufer.addBufer(word);  //Добовляем сообщение в историю

/*
                    for (ServerSomthing vr : Server.serverList) {
                        vr.send(word); // отослать принятое сообщение с привязанного клиента всем остальным влючая его
                    }*/
                }
            } catch (NullPointerException ignored) {}


        } catch (IOException e) {
            this.stopService();
        }
    }


    public void send(String msg) {
        try {
            out.write(msg + "\n");
            out.flush();
        } catch (IOException ignored) {}

    }

    private void stopService() {
        try {
            if(!socket.isClosed()) {
                socket.close();
                in.close();
                out.close();
                for (ServerSomthing vr : Server.serverList) {
                    if(vr.equals(this)) vr.interrupt();
                    Server.serverList.remove(this);
                }
            }
        } catch (IOException ignored) {}
    }
}
