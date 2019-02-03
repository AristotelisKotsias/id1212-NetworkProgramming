package db.jpa.common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Specifies the file-system's remote methods.
 */
public interface FileSystem extends Remote {

    final String FILESYSTEM_NAME_IN_REGISTRY = "filesystem";

    void registerClient(String username, String password) throws RemoteException;

    void unregisterClient(String username, String password) throws RemoteException;

    ClientDTO loginClient(String username, String password) throws RemoteException;

    List<? extends FileDTO> listFiles() throws RemoteException;
    
    List<? extends FileDTO> listFiles(ClientDTO owner) throws RemoteException;

    void uploadFile(ClientDTO owner, String name, int size, boolean isPrivate, boolean isWritable, boolean isReadable) throws RemoteException;
    
    boolean downloadFile(ClientDTO user, String name) throws RemoteException;
    
    void updateFile(ClientDTO owner, String name, int size, boolean isPrivate, boolean isWritable, boolean isReadble) throws RemoteException;

    void deleteFile(ClientDTO client, String name) throws RemoteException;
    
    void notify(ClientDTO client, String name, Alert a) throws RemoteException;
}
