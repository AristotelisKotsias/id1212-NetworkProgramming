package db.jpa.common;

import db.jpa.server.model.Client;
import java.io.Serializable;

public interface FileDTO extends Serializable {

    public Client getOwner();

    public String getName();

    public boolean hasPrivateAccess();

    public boolean hasReadPermission();

    public boolean hasWritePermission();

    public long getSize();

}