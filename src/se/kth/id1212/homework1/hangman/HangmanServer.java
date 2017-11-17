/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.homework1.hangman;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author udde
 */
public class HangmanServer {
    
    private int portNum = 3000;
    private int LINGER_TIME = 10000;
    

    
    private void acceptPlayers(){
        
        try {
            ServerSocket listener = new ServerSocket(portNum);
            while (true) {
                Socket playerSocket = listener.accept();
                playerSocket.setSoLinger(true, LINGER_TIME);
                Thread handler = new Thread(new SessionHandler(playerSocket));
                handler.setPriority(Thread.MAX_PRIORITY);
                handler.start();
            }
        } catch (IOException e) {
            System.err.println("Server error.");
        }
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        HangmanServer hangman = new HangmanServer();
        hangman.acceptPlayers();
    }
    
}
