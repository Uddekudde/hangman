/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.kth.id1212.homework1.hangman;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author udde
 */
public class SessionHandler implements Runnable {
    
    private Socket playerSocket;
    private boolean isConnected;
    private BufferedReader fromPlayer;
    private PrintWriter toPlayer;
    
    private final String GUESS = "#Guess";
    private final String START = "#Start";
    private final String QUIT = "#Quit";

    public SessionHandler(Socket playerSocket) {
        this.playerSocket = playerSocket;
        isConnected = true;
    }
    
    @Override
    public void run() {
        Hangman hangmanGame = new Hangman();
        try {
            boolean autoFlush = true;
            fromPlayer = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            toPlayer = new PrintWriter(playerSocket.getOutputStream(), autoFlush);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        while(isConnected){
            try {
                String action = fromPlayer.readLine();
                switch(action){
                    case START:
                         sendMessage(hangmanGame.start());
                        break;
                    case QUIT:
                        playerSocket.close();
                        playerSocket = null;
                        isConnected = false;
                    default:
                        sendMessage(hangmanGame.guess(action));
                        break;
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
    
    private void sendMessage(String action) throws IOException{
        toPlayer.println(action);
        toPlayer.flush();
    }
    
}
