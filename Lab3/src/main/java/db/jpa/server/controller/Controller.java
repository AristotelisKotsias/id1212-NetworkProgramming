package db.jpa.server.controller;

import db.jpa.common.*;
import db.jpa.server.integration.ClientDAO;
import db.jpa.server.integration.FileDAO;
import db.jpa.server.model.Client;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import db.jpa.common.ClientDTO;
import db.jpa.common.FileDTO;
import db.jpa.server.model.File;
import java.util.HashMap;

public class Controller extends UnicastRemoteObject implements FileSystem {

    private HashMap<Integer, Alert> notifyOwnerhm = new HashMap();
    private final FileDAO fileDAO;
    private final ClientDAO clientDAO;

    public Controller() throws RemoteException {
        super();
        this.fileDAO = new FileDAO();
        this.clientDAO = new ClientDAO();
    }

    @Override
    public void registerClient(String username, String password) throws RemoteException {
        if (clientDAO.findCliendByName(username) == null) {
            clientDAO.registerClient(new Client(username, password));
        } else {
            throw new RemoteException("Username '" + username + "' is in use. Pick a new username.");
        }
    }

    @Override
    public void unregisterClient(String username, String password) throws RemoteException {
        Client client = clientDAO.findCliendByName(username);
        if (client != null && clientDAO.findCliendByPass(password) != null) {
            clientDAO.deleteClient(client);
        } else {
            throw new RemoteException("Invalid credentials. Please, try again.");
        }
    }

    @Override
    public ClientDTO loginClient(String username, String password) throws RemoteException {
        Client client = clientDAO.findCliendByName(username);
        if (client != null && clientDAO.findCliendByPass(password) != null) {
            return client;
        } else {
            throw new RemoteException("Invalid credentials. Please, try again.");
        }
    }

    @Override
    public void uploadFile(ClientDTO owner, String name, int size, boolean isPrivate, boolean isWritable, boolean isReadable) throws RemoteException {
        if (fileDAO.findFileByName(name) == null) {
            fileDAO.storeFile(new File(clientDAO.findCliendByName(owner.getUsername()), name, isPrivate, isWritable, isReadable, size));
        } else {
            throw new RemoteException("The file '" + name + "' already exists.");
        }
    }

    @Override
    public boolean downloadFile(ClientDTO clientDTO, String name) throws RemoteException {
        Client client = clientDAO.findCliendByName(clientDTO.getUsername());
        File file = fileDAO.findFileByName(name);
        if (file == null) {
            throw new RemoteException("File '" + name + "' does not exist.");
        } else if (file.getOwner().getId() == client.getId() || (file.hasReadPermission() && !file.hasPrivateAccess())) {
            if (notifyOwnerhm.containsKey(file.getId())) {
                Alert al = notifyOwnerhm.get(file.getId());
                al.alertMessage("File '" + name + "' has been downloaded by a user.");
            }
            return true;
        } else {
            throw new RemoteException("You have no permissions.");
        }

    }

    @Override
    public void deleteFile(ClientDTO clientDTO, String name) throws RemoteException {
        Client client = clientDAO.findCliendByName(clientDTO.getUsername());
        File file = fileDAO.findFileByName(name);

        if (file == null) {
            throw new RemoteException("File '" + name + "' does not exist.");
        } else if (file.getOwner().getId() == client.getId() || (!file.hasPrivateAccess())) {
            fileDAO.deleteFile(file);
            if (notifyOwnerhm.containsKey(file.getId())) {
                Alert al = notifyOwnerhm.get(file.getId());
                al.alertMessage("File '" + name + "' has been deleted by a user.");
            }
        } else {
            throw new RemoteException("You have no permissions.");
        }
    }

    @Override
    public List<? extends FileDTO> listFiles() throws RemoteException {
        return listFiles(null);
    }

    @Override
    public List<? extends FileDTO> listFiles(ClientDTO owner) {
        Client client;
        if (owner != null) {
            client = clientDAO.findCliendByName(owner.getUsername());
        } else {
            client = null;
        }
        return fileDAO.listAllFiles(client);
    }

    @Override
    public void updateFile(ClientDTO clientDTO, String name, int size, boolean isPrivate, boolean isWritable, boolean isReadble) throws RemoteException {
        Client client = clientDAO.findCliendByName(clientDTO.getUsername());
        File file = fileDAO.findFileByName(name);

        if (file == null) {
            throw new RemoteException("File '" + name + "' does not exist.");
        } else if (file.getOwner().getId() == client.getId() || (file.hasWritePermission() && !file.hasPrivateAccess())) {
            fileDAO.updateFile(file);
            if (notifyOwnerhm.containsKey(file.getId())) {
                Alert al = notifyOwnerhm.get(file.getId());
                al.alertMessage("File '" + name + "' has been updated by a user.");
            }
        } else {
            throw new RemoteException("You have no permissions.");
        }
    }

    @Override
    public void notify(ClientDTO clientDTO, String name, Alert a) throws RemoteException {

        File file = fileDAO.findFileByName(name);
        Client client = clientDAO.findCliendByName(clientDTO.getUsername());
        if (file == null) {
            throw new RemoteException("File does not exit.");
        } else if (client.getId() == file.getOwner().getId()) {
            notifyOwnerhm.put(file.getId(), a);
        } else {
            throw new RemoteException("You have no permissions.");
        }
    }

}