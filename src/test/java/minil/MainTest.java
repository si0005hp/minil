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

import org.antlr.v4.runtime.CharStreams;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;

public class MainTest {

    private ByteArrayOutputStream bos;
    private PrintStream sysout;

    @Before
    public void setUp() {
        bos = new ByteArrayOutputStream();
        sysout = System.out;
        System.setOut(new PrintStream(new BufferedOutputStream(bos)));
    }

    @After
    public void tearDown() {
        System.setOut(sysout);
    }

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
        
        testdata.stream().map(Entry::getKey).forEach(this::run);
        System.out.flush();
        
        String expected = testdata.stream().map(Entry::getValue).map(String::valueOf)
                .collect(Collectors.joining(System.lineSeparator())) + System.lineSeparator();
        
        assertThat(bos.toString(), is(expected));
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
}
