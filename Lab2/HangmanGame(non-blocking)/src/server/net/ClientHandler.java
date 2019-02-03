/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.Controller;

public class ClientHandler implements Runnable {

    private final ByteBuffer msgFromClient = ByteBuffer.allocateDirect(8192);
    private final Queue<String> stringForProcess = new ArrayDeque<>();
    private final Queue<ByteBuffer> msgToClient = new ArrayDeque<>();
    private SocketChannel clientChannel;
    private SelectionKey key;
    private Selector selector;
    private Controller ctrl;
    
    
    ClientHandler(SocketChannel clientChannel, Selector selector) throws IOException {
        this.selector = selector;
        ctrl = new Controller();
        this.clientChannel = clientChannel;
    }
    
    @Override
    public void run() {
            try {
                msgToClient.add(ByteBuffer.wrap(ctrl.sendInput(stringForProcess.remove()).getBytes()));
                key.interestOps(SelectionKey.OP_WRITE);
                selector.wakeup();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    void receiveInput(SelectionKey key) throws IOException {
        this.key = key;
        msgFromClient.clear();
        int numOfReadBytes;
        numOfReadBytes = clientChannel.read(msgFromClient);
        if(numOfReadBytes == -1) {
            throw new IOException("Client has closed connection.");
        }
        String receivedInput = extractMessageFromBuffer();
        stringForProcess.add(receivedInput);
        ForkJoinPool.commonPool().execute(this);
    }

    private String extractMessageFromBuffer() {
        msgFromClient.flip();
        byte[] bytes = new byte[msgFromClient.remaining()];
        msgFromClient.get(bytes);
        return new String(bytes);
    }

    
    void sendServerOutput() throws IOException {
        while(!msgToClient.isEmpty()) {
            ByteBuffer output = msgToClient.remove();
            clientChannel.write(output);
            if(output.hasRemaining()) {
                return;
            }
        }           
    }

    void disconnectClient() throws IOException {
        clientChannel.close();
    } 
}   