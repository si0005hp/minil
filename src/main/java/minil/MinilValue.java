package minil;

import lombok.Value;

@Value
public final class MinilValue {
    public enum ValueType {
        INT,
        STRING
    }
    
    private final ValueType type;
    private final Object value;
    
    public int asInt() {
        return (int)value;
    }
    
    public String asStr() {
        return (String)value;
    }
}
