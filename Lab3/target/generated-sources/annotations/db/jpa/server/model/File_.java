package db.jpa.server.model;

import db.jpa.server.model.Client;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-12-03T18:40:32")
@StaticMetamodel(File.class)
public class File_ { 

    public static volatile SingularAttribute<File, Client> owner;
    public static volatile SingularAttribute<File, Long> size;
    public static volatile SingularAttribute<File, String> name;
    public static volatile SingularAttribute<File, Integer> id;
    public static volatile SingularAttribute<File, Boolean> isPrivate;
    public static volatile SingularAttribute<File, Boolean> isWritable;
    public static volatile SingularAttribute<File, Boolean> isReadable;

}