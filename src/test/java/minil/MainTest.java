package minil;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.antlr.v4.runtime.CharStreams;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class MainTest {
    
    private static final String CR = System.lineSeparator();

    @Test
    public void arithmeticOpes() {
        List<Entry<String, Integer>> testdata = ImmutableMap.<String, Integer> builder()
                .put("1",1)
                .put("1+2",3)
                .put("1+2+3+4+5",15)
                .put("3-1",2)
                .put("10-3-2-1",4)
                .put("5+3-2+1-4+9",12)
                .put("2*5",10)
                .put("2*3*4",24)
                .put("1*2+3+4*5",25)
                .put("1*2+3-4*5",-15)
                .put("10/2",5)
                .put("99/3/11",3)
                .put("10/2*3+5-3+4*2/8",18) 
                .build().entrySet().stream().collect(Collectors.toList());
        
        String output = runAndGetSysout(() -> testdata.stream().map(Entry::getKey).forEach(this::run));
        String expected = perNewLine(testdata.stream().map(Entry::getValue).collect(Collectors.toList()).toArray());
        
        assertThat(output, is(expected));
    }
    
    @Test
    public void var() {
        assertThat(runAndGetSysout(() -> runF("var/var1.minil")), is(perNewLine(999, 7)));
        assertThat(runAndGetSysout(() -> runF("var/var2.minil")), is(perNewLine(999, 7)));
        assertThat(runAndGetSysout(() -> runF("var/var3.minil")), is(perNewLine(1006)));
        assertThat(runAndGetSysout(() -> runF("var/var4.minil")), is(perNewLine(1028)));
    }
    
    @Test
    public void func() {
        assertThat(runAndGetSysout(() -> runF("func/func1.minil")), is(perNewLine(1)));
        assertThat(runAndGetSysout(() -> runF("func/func2.minil")), is(perNewLine(1, 9, 8, 7)));
        assertThat(runAndGetSysout(() -> runF("func/func3.minil")), is(perNewLine(7, 65, 999)));
        assertThat(runAndGetSysout(() -> runF("func/func4.minil")), is(perNewLine(999, 24)));
        expectedToFail(() -> runF("func/func5.minil"));
        assertThat(runAndGetSysout(() -> runF("func/func6.minil")), is(perNewLine(3960)));
        assertThat(runAndGetSysout(() -> runF("func/func7.minil")), is(perNewLine(720)));
    }
    
    @Test
    public void ifstmt() {
        assertThat(runAndGetSysout(() -> runF("ifstmt/if1.minil")), is(perNewLine(9)));
        assertThat(runAndGetSysout(() -> runF("ifstmt/if2.minil")), is(perNewLine(5, 9)));
        assertThat(runAndGetSysout(() -> runF("ifstmt/if3.minil")), is(perNewLine(3)));
        assertThat(runAndGetSysout(() -> runF("ifstmt/if4.minil")), is(perNewLine(1)));
        assertThat(runAndGetSysout(() -> runF("ifstmt/if5.minil")), is(perNewLine(9, 1)));
        assertThat(runAndGetSysout(() -> runF("ifstmt/if6.minil")), is(perNewLine(200, 1)));
        assertThat(runAndGetSysout(() -> runF("ifstmt/if7.minil")), is(perNewLine(1, 200)));
        assertThat(runAndGetSysout(() -> runF("ifstmt/if8.minil")), is(perNewLine(1, 2, 2, 1, 1, 1, 1, 2, 1)));
    }
    
    @Test
    public void whilestmt() {
        assertThat(runAndGetSysout(() -> runF("whilestmt/while1.minil")), is(perNewLine(0, 1, 2)));
        assertThat(runAndGetSysout(() -> runF("whilestmt/while2.minil")), is(perNewLine(0, 1, 2, 3, 4)));
        assertThat(runAndGetSysout(() -> runF("whilestmt/while3.minil")), is(perNewLine(0, 1, 2, 0, 1, 2)));
    }
    
    private void expectedToFail(Runnable r) {
        try {
            r.run();
        } catch (Exception e) {
            return;
        }
        throw new RuntimeException("Expected to be failed but normaly end.");
    }
    
    private String runAndGetSysout(Runnable r) {
        PrintStream orgSysout = System.out;
        
        String result = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(new BufferedOutputStream(bos));) {
            System.setOut(ps);
            r.run();
            System.out.flush();
            result = bos.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            System.setOut(orgSysout);
        }
        return result;
    }
    
    private int run(String s) {
        String p = String.format("print(%s)", s);
        return Main.run(CharStreams.fromString(p));
    }

    private int runF(String s) {
        try (InputStream is = getClass().getResourceAsStream(s)) {
            return Main.run(CharStreams.fromStream(is));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file: " + s);
        }
    }
    
    private String perNewLine(Object... data) {
        if (data.length == 0) {
            return "";
        }
        return Stream.of(data).map(String::valueOf).collect(Collectors.joining(CR)) + CR;
    }
    
}
