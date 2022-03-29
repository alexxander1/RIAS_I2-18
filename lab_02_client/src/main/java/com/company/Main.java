
package com.company;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.lang.invoke.MethodHandles;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.IntStream;
import java.util.stream.LongStream;


import java.util.Scanner;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());



    public static String ipAddr = "localhost";
    public static int port = 1502;
    public static ClientSomthing client;

    public static boolean ServerConnected = false;

    public static void main(String[] args) {
        logger.info("Start App");

        client = new ClientSomthing(ipAddr, port);

        //StringTokenizer
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a command: ");
        while (scanner.hasNext()) {
            String input = scanner.nextLine();
            String[] inputParameters = input.split(" ");
            switch (inputParameters[0].trim()) {
                case "connect":
                    try {
                        connectToServer(inputParameters[1].trim(), inputParameters[2].trim());
                    } catch (Exception e) {
                        System.err.println("Check your ip and port!");
                        System.out.println("connect [ip] [port]");
                        logger.debug("Exception connect: " + input);
                    }
                    break;
                case "send":
                    sendToServer(input.substring(inputParameters[0].trim().length()));
                    break;
                case "disconnect":
                    disconnectFromServer();
                    break;
                case "help":
                    printHelp();
                    break;
                case "quit":
                    logger.debug("Exit form app");
                    System.out.println("Exit form app");
                    System.exit(0);
                    break;
                default:
                    printError();
                    printHelp();
            }
        }
    }

    private static void printError() {
        System.out.println("Error");
    }

    private static void printHelp() {
        System.out.println("Help");

        System.out.println("connect         Подключение серверу");
        System.out.println("connect [ip] [port]");
        System.out.println("send            Отправка сообщения на сервер");
        System.out.println("send [message]");
        System.out.println("disconnect      Отключение от сервера");
        System.out.println("disconnect");
        System.out.println("help            Вывод списка help");
        System.out.println("help");
        System.out.println("quit            Выход из приложения");
        System.out.println("quit");

    }

    private static void disconnectFromServer() {
        ServerConnected = false;
        client.stopService();
        logger.debug("Successfully disconnected the server");
        System.out.println("Successfully disconnected the server");
    }

    private static void sendToServer(String message) {
        logger.debug("sendToServer: " + message);
        if (ServerConnected = true) {
            client.sendMes(message);
            System.out.println("Successfully sent message: " + message);
        }
        else{
            System.err.println("Error! Not connected!");
        }
    }

    private static void connectToServer(String ip, String port) {
        logger.debug("connectToServer: " + ip + " " + port);

        Integer Port = 1502;

        try{
            Port = Integer.parseInt(port);
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }

        if(client.connect(ip, Port))
        {ServerConnected = true;}
        else {ServerConnected = false;}

        logger.debug("Successfully connected to server: " + ip + " " +  port);
        System.out.println("Successfully connected to server with ip = " + ip + " and port = " + port);
    }
}