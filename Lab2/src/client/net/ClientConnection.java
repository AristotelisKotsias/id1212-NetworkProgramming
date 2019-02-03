package client.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

public class ClientConnection implements Runnable {

    private final Queue<ByteBuffer> messagesToSend = new ArrayDeque<>();
    private final ByteBuffer msgFromServer = ByteBuffer.allocateDirect(8192);
    private final Queue<String> msgForClient = new ArrayDeque<>();
    private InetSocketAddress serverAddress;
    private SocketChannel socketChannel;
    private Selector selector;
    private boolean connected;
    private volatile boolean timeToSend = false;

    @Override
    public void run() {
        try {
            initConnection();
            initSelector();

            while (connected || !messagesToSend.isEmpty()) {
                if (timeToSend) {
                    socketChannel.keyFor(selector).interestOps(SelectionKey.OP_WRITE);
                    timeToSend = false;
                }

                selector.select();
                for (SelectionKey key : selector.selectedKeys()) {
                    selector.selectedKeys().remove(key);
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isConnectable()) {
                        completeConnection(key);
                    } else if (key.isReadable()) {
                        recvFromServer();
                    } else if (key.isWritable()) {
                        sendToServer(key);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Game ended");
            System.exit(0);
        }
    }

    public void connectToServer(String host, int port) {
        serverAddress = new InetSocketAddress(host, port);
        new Thread(this).start();
    }

    private void initSelector() throws IOException {
        selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_CONNECT);
    }

    private void initConnection() throws IOException {
        socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);
        socketChannel.connect(serverAddress);
        connected = true;
    }

    private void completeConnection(SelectionKey key) throws IOException {
        socketChannel.finishConnect();
        key.interestOps(SelectionKey.OP_READ);
        try {
            InetSocketAddress remoteAddress = (InetSocketAddress) socketChannel.getRemoteAddress();
            notifyConnectionDone(remoteAddress);
        } catch (IOException e) {
            notifyConnectionDone(serverAddress);
        }
    }

    private void recvFromServer() throws IOException {
        msgFromServer.clear();
        int numOfReadBytes;
        numOfReadBytes = socketChannel.read(msgFromServer);
        if (numOfReadBytes == -1) {
            throw new IOException("Error while receiving message.");
        }
        String receivedInput = extractOutputFromBuffer();
        msgForClient.add(receivedInput);
        while (!msgForClient.isEmpty()) {
            replyFromServer(msgForClient.remove());
        }
    }

    private String extractOutputFromBuffer() {
        msgFromServer.flip();
        byte[] bytes = new byte[msgFromServer.remaining()];
        msgFromServer.get(bytes);
        return new String(bytes);
    }

    private void sendToServer(SelectionKey key) throws IOException {
        ByteBuffer input;
        synchronized (messagesToSend) {
            while ((input = messagesToSend.peek()) != null) {
                socketChannel.write(input);
                if (input.hasRemaining()) {
                    return;
                }
                messagesToSend.remove();
            }
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    public void sendClientInput(String input) throws IOException {   
        if (input.toLowerCase().equals("exit game")) {
            disconnect();
        }
        synchronized (messagesToSend) {
            messagesToSend.add(ByteBuffer.wrap(input.getBytes()));
        }
        timeToSend = true;
        selector.wakeup();
    }

    private void replyFromServer(String output) {
        Executor pool = ForkJoinPool.commonPool();
        pool.execute(() -> {
            System.out.println(output);
        });
    }

    private void notifyConnectionDone(InetSocketAddress address) {
        Executor pool = ForkJoinPool.commonPool();
        pool.execute(() -> {
            System.out.println("\nYou are now connected to the server " + address);
        });
    }

    private void notifyDisconnectionDone() throws IOException {
        Executor pool = ForkJoinPool.commonPool();
        pool.execute(() -> {
            System.out.println("You have exited the game");
        });
    }

    public void disconnect() throws IOException {
        socketChannel.close();
        socketChannel.keyFor(selector).cancel();
        connected = false;
        notifyDisconnectionDone();
    }
}