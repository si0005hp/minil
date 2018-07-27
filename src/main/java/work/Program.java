package work;

import java.util.List;

import lombok.Getter;

@Getter
public class Program {
    private final List<String> args;
    
    public Program(List<String> args) {
        this.args = args;
    }
}
