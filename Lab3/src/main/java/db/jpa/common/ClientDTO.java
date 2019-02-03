package db.jpa.common;

import db.jpa.server.model.File;
import java.io.Serializable;
import java.util.List;

public interface ClientDTO extends Serializable {

    public int getId();

    public String getUsername();

    public String getPassword();

    public List<File> getFiles();
    
}