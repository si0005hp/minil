package minil;

import java.util.ArrayList;

import lombok.Value;

@Value
public final class MinilValue {
    public enum ValueType {
        INT,
        STRING,
        ARRAY
    }
    
    private final ValueType type;
    private final Object value;
    
    public int asInt() {
        return (int)value;
    }
    
    public String asStr() {
        return (String)value;
    }
    
    @SuppressWarnings(value = { "unchecked" })
    public ArrayList<MinilValue> asArray() {
        return (ArrayList<MinilValue>)value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
    
}
