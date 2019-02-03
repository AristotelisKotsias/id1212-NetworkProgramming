package db.jpa.client.view;

public enum Command {   
    REGISTER,
    UNREGISTER,
    LOGIN,
    LOGOUT,
    UPLOAD_FILE,
    DOWNLOAD_FILE,
    LIST_FILES,
    DELETE_FILE,
    UPDATE_FILE,
    NOTIFY,
    HELP,
    ILLEGAL_COMMAND,
    QUIT
}
