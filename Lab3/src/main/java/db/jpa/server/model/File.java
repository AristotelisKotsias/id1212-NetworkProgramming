package db.jpa.server.model;

import db.jpa.common.FileDTO;
import javax.persistence.*;

@Entity(name = "File")
public class File implements FileDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Client owner;

    @Column(unique = true, nullable = false)
    private String name;

    private long size;
    
    private boolean isPrivate;

    private boolean isReadable;

    private boolean isWritable;

    public File() {

    }

    public File(Client owner, String name,  boolean isPrivate, boolean isReadable, boolean isWritable,long size) {
        this.owner = owner;
        this.name = name;
        this.isPrivate = isPrivate;
        this.isReadable = isReadable;
        this.isWritable = isWritable;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }

    public long getSize() {
        return size;
    }
    
    public void setSize(long size) {
        this.size = size;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasPrivateAccess() {
        return isPrivate;
    }

    public void setPrivateAccess(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    public boolean hasReadPermission() {
        return isReadable;
    }

    public void setReadPermission(boolean isReadable) {
        this.isReadable = isReadable;
    }

    public boolean hasWritePermission() {
        return isWritable;
    }

    public void setWritePermission(boolean isWritable) {
        this.isWritable = isWritable;
    }
}
