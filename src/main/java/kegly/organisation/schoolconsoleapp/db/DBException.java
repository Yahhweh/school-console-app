package kegly.organisation.schoolconsoleapp.db;

public class DBException extends RuntimeException {

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBException(String message) {
        super(message);
    }

}