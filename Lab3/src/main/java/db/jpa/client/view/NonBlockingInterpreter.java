package db.jpa.client.view;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Scanner;
import db.jpa.common.*;

public class NonBlockingInterpreter implements Runnable {

    private static final String PROMPT = "> ";
    private final Scanner input = new Scanner(System.in);
    private FileSystem filesystem;
    private ClientDTO client = null;
    private boolean loggedIn = false;
    private String username;
    private String password;
    private String nameOfFile;
    private int sizeOfFile;
    private boolean isPrivate;
    private boolean isWritable;
    private boolean isReadable;
    private boolean download;
    private NotificationHandler notif;
    private final ThreadSafeStdOut outMgr = new ThreadSafeStdOut();

    public void start(FileSystem file) {
        this.filesystem = file;
        try {
            this.notif = new NotificationHandler();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (loggedIn) {
            return;
        }
        loggedIn = true;

        new Thread(this).start();
    }

    @Override
    public void run() {
        outMgr.println("~~Welcome to the File Catalog~~");
        while (loggedIn) {
            try {
                System.out.println();
                outMgr.println("Enter command: ('help' for list of commands)");
                CmdLine cmdLine = new CmdLine(readNextLine());

                switch (cmdLine.getCmd()) {
                    case HELP:
                        System.out.println("~~The commands are~~");
                        for (Command command : Command.values()) {
                            if (command == Command.ILLEGAL_COMMAND) {
                                continue;
                            }
                            System.out.println(command.toString().toLowerCase());
                        }
                        break;
                    case REGISTER:
                        outMgr.println("Choose a username: ");
                        username = readNextLine();
                        outMgr.println("Choose a password: ");
                        password = readNextLine();
                        filesystem.registerClient(username, password);
                        outMgr.println("User '" + username + "' has been registered to the file catalog.");
                        break;
                    case UNREGISTER:
                        outMgr.println("Username: ");
                        username = readNextLine();
                        outMgr.println("Password: ");
                        password = readNextLine();
                        filesystem.unregisterClient(username, password);
                        this.client = null;
                        outMgr.println("User: '" + username + "' has been unregistered");
                        break;
                    case LOGIN:
                        outMgr.println("Enter username: ");
                        username = readNextLine();
                        outMgr.println("Enter password: ");
                        password = readNextLine();
                        this.client = filesystem.loginClient(username, password);
                        outMgr.println("You have logged in the file catalog.");
                        break;
                    case LOGOUT:
                        this.client = null;
                        outMgr.println("You have logged out the file catalog.");
                        break;
                    case UPLOAD_FILE:
                        if (this.client != null) {
                            outMgr.println("Name of file: ");
                            nameOfFile = readNextLine();
                            outMgr.println("Size of file: ");
                            sizeOfFile = readNextInt();
                            outMgr.println("Is the file private(true/false): ");
                            isPrivate = readNextBoolean();
                            outMgr.println("Is the file writable(true/false): ");
                            isWritable = readNextBoolean();
                            outMgr.println("Is the file readable(true/false): ");
                            isReadable = readNextBoolean();
                            filesystem.uploadFile(this.client, nameOfFile, sizeOfFile, isPrivate, isWritable, isReadable);
                            outMgr.println("File uploaded!");
                        } else {
                            outMgr.println("You must be logged in to upload a file.");
                        }
                        break;
                    case LIST_FILES:
                        List<? extends FileDTO> list;
                        if (this.client != null) {
                            list = filesystem.listFiles(client);
                        } else {
                            list = filesystem.listFiles();
                        }
                        outMgr.println("NAME (SIZE) - PRIVATE|WRITE|READ");
                        for (FileDTO file : list) {
                            outMgr.println(file.getName() + " (" + file.getSize() + "bytes) - " + file.hasPrivateAccess() + "|" + file.hasWritePermission() + "|" + file.hasReadPermission());
                        }
                        break;
                    case DOWNLOAD_FILE:
                        if (this.client != null) {
                            outMgr.println("Name of the file that you wish to download.");
                            nameOfFile = readNextLine();
                            download = filesystem.downloadFile(client, nameOfFile);
                            if (download) {
                                outMgr.println("File '" + nameOfFile + "' has been downloaded.");
                            }
                        } else {
                            outMgr.println("You must be logged in.");
                        }
                        break;
                    case DELETE_FILE:
                        outMgr.println("Name of the file that you wish to delete.");
                        nameOfFile = readNextLine();
                        if (this.client != null) {
                            filesystem.deleteFile(client, nameOfFile);
                            outMgr.println("File has been deleted.");
                        } else {
                            outMgr.println("You must be logged in.");
                        }
                        break;
                    case UPDATE_FILE:
                        if (this.client != null) {
                            outMgr.println("Name of file: ");
                            nameOfFile = readNextLine();
                            outMgr.println("Size of file: ");
                            sizeOfFile = readNextInt();
                            outMgr.println("Is the file private(true/false): ");
                            isPrivate = readNextBoolean();
                            outMgr.println("Is the file writable(true/false): ");
                            isWritable = readNextBoolean();
                            outMgr.println("Is the file readable(true/false): ");
                            isReadable = readNextBoolean();
                            filesystem.updateFile(this.client, nameOfFile, sizeOfFile, isPrivate, isWritable, isReadable);
                            outMgr.println("File has been updated.");
                        } else {
                            outMgr.println("You must be logged in.");
                        }
                        break;
                    case NOTIFY:
                        if (this.client != null) {
                            outMgr.println("Name of file that you want to be notified: ");
                            nameOfFile = readNextLine();
                            filesystem.notify(this.client, nameOfFile, this.notif);
                        } else {
                            outMgr.println("You must be logged in.");
                        }
                        break;
                    case QUIT:
                        outMgr.println("Exitting the file catalog...");
                        loggedIn = false;
                        break;
                    case ILLEGAL_COMMAND:
                        break;
                }
            } catch (Exception e) {
                outMgr.println("Operation failed");
                outMgr.println(e.getMessage());
            }

        }
    }

    private String readNextLine() {
        outMgr.print(PROMPT);
        return input.nextLine();
    }

    private int readNextInt() {
        outMgr.print(PROMPT);
        return input.nextInt();
    }

    private boolean readNextBoolean() {
        outMgr.print(PROMPT);
        return input.nextBoolean();
    }

    private class NotificationHandler extends UnicastRemoteObject implements Alert {

        public NotificationHandler() throws RemoteException {
        }

        @Override
        public void alertMessage(String msg) throws RemoteException {
            outMgr.println(msg);
        }
    }

}
