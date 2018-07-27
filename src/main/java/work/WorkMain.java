package work;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class WorkMain {
    private static final String MODE = "view";
    
    public static void main(String[] args) throws IOException {
        try (InputStream is = args.length < 1 ?
                        System.in : new FileInputStream(args[0]);) {
            
            if (MODE.equals("view")) {
                treeView(CharStreams.fromStream(is));
            } else {
                int res = run(CharStreams.fromStream(is));
//                System.out.println(res);
            }
        }
    }
    
    static int run(CharStream input) {
        System.out.println(getParser(input).program().n.getArgs());
        return 0;
    }
    
    private static WorkParser getParser(CharStream input) {
        WorkLexer lexer = new WorkLexer(input);
        WorkParser parser = new WorkParser(new CommonTokenStream(lexer));
        return parser;
    }
    
    private static void treeView(CharStream input) {
        WorkParser parser = getParser(input);
        TreeViewer v = new TreeViewer(Arrays.asList(parser.getRuleNames()), parser.program());
        v.open();
    }

}
