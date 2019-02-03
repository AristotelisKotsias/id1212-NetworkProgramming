package db.jpa.client.startup;

import db.jpa.client.view.*;
import db.jpa.common.FileSystem;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) {
        try {
            FileSystem fs = (FileSystem) Naming.lookup(FileSystem.FILESYSTEM_NAME_IN_REGISTRY);
            new NonBlockingInterpreter().start(fs);
        } catch (NotBoundException | MalformedURLException | RemoteException e) {
            e.printStackTrace();
        }
    }

}
