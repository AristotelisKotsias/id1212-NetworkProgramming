package db.jpa.common;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Alert extends Remote {

    void alertMessage(String msg) throws RemoteException;
}
