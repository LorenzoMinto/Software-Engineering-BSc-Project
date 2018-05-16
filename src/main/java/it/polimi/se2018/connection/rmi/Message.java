package it.polimi.se2018.connection.rmi;

import java.io.Serializable;

public class Message implements Serializable {
    
    static final long serialVersionUID = 43L;

    private String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
