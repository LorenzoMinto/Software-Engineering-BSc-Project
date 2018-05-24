package it.polimi.se2018.utils.message;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Message implements Serializable{

    private Enum type;

    private HashMap<String,Object> params;

    public Message(Enum type){
        this(type,null);
    }

    public Message(Enum type, Map<String, Object> params) {
        this.type = type;
        this.params = (HashMap<String,Object>)params;
    }

    public Enum getType() {
        return type;
    }

    public Object getParam(String key){
        return this.params.get(key);
    }
}
