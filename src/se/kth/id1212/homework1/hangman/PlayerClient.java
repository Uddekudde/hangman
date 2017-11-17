/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.homework1.hangman;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author udde
 */
public class PlayerClient {
    String hangmanServer = "127.0.0.1";
    int serverPort = 3000;
    int milliseconds = 150000;
    Socket serverSocket;
    BufferedReader fromServer;
    PrintWriter toServer;
    boolean isConnected = true;
    
    public static void main(String[] args) {
     PlayerClient playerClient = new PlayerClient();
     Interpreter interpreter = playerClient.new Interpreter();
     System.out.println("Type #Start to start a game, then type your guess. "
             + "Type #Quit at any time to close the program.");
     
     CompletableFuture.runAsync(() -> {
                playerClient.connect();
        });
     
     interpreter.run();
    
    }
    
    private void connect(){
        try {
            serverSocket = new Socket(hangmanServer, serverPort);
            toServer = new PrintWriter(serverSocket.getOutputStream());
            fromServer = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            serverSocket.setSoTimeout(milliseconds);
            CompletableFuture.runAsync(() -> {
                listen();
            });
            
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    private void sendMessage(String message){
            CompletableFuture.runAsync(() -> {
                toServer.println(message);
                toServer.flush();
                if(message.equals("#Quit")){
                    disconnect();
                }
            });
    }
    
    private class Interpreter {
         final String START = "#Start";
         final String GUESS = "#Guess";
         final String QUIT = "#Quit";
        Scanner input = new Scanner(System.in);

        public void run(){
            String str;
            while(isConnected){
                switch(str = input.nextLine()){
                    case QUIT:
                        sendMessage(QUIT);
                        isConnected = false;
                        break;
                    default:
                        sendMessage(str);
                        break;
                }
            }
        }
    }
    
    private void disconnect(){
        try {
            serverSocket.close();
            serverSocket = null;
        } catch (IOException ex) {
            System.out.println("There was an error quitting.");
        }
    }
    
    //@Override
        public void listen() {
            try {
                String str;
                while(isConnected) {
                    if((str = fromServer.readLine()) != null){
                       System.out.println(str);
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("FAIL!");
            }
        }
}
