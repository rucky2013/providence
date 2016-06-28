package net.morimekta.providence.compiler;

import net.morimekta.util.io.IOUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by morimekta on 4/26/16.
 */
public class CompilerTest {
    private static InputStream defaultIn;
    private static PrintStream defaultOut;
    private static PrintStream defaultErr;

    @Rule
    public TemporaryFolder temp;

    private OutputStream outContent;
    private OutputStream errContent;

    private int      exitCode;
    private Compiler compiler;
    private File     thriftFile;
    private File     refFile;
    private String   version;
    private File     include;
    private File     output;

    @BeforeClass
    public static void setUpIO() {
        defaultIn = System.in;
        defaultOut = System.out;
        defaultErr = System.err;
    }

    @Before
    public void setUp() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/build.properties"));
        version = properties.getProperty("build.version");

        temp = new TemporaryFolder();
        temp.create();

        include = temp.newFolder("include");
        output = temp.newFolder("output");

        refFile = new File(include, "ref.thrift");
        thriftFile = temp.newFile("test.thrift");

        FileOutputStream file = new FileOutputStream(thriftFile);
        IOUtils.copy(getClass().getResourceAsStream("/compiler/test.thrift"), file);
        file.flush();
        file.close();

        file = new FileOutputStream(refFile);
        IOUtils.copy(getClass().getResourceAsStream("/compiler/ref.thrift"), file);
        file.flush();
        file.close();

        exitCode = 0;

        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();

        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        compiler = new Compiler() {
            @Override
            protected void exit(int i) {
                exitCode = i;
            }
        };
    }

    @After
    public void tearDown() {
        System.setErr(defaultErr);
        System.setOut(defaultOut);
        System.setIn(defaultIn);
    }

    @Test
    public void testHelp() {
        compiler.run("--help");

        assertEquals("Providence compiler - v" + version + "\n" +
                     "Usage: pvdc [-I dir] [-o dir] -g generator[:opt[,opt]*] file...\n" +
                     "\n" +
                     "Example code to run:\n" +
                     "$ pvdc -I thrift/ --out target/ --gen java:android thrift/the-one.thrift\n" +
                     "\n" +
                     " --gen (-g) generator       : Generate files for this language spec.\n" +
                     " --help (-h, -?) [language] : Show this help or about language.\n" +
                     " --include (-I) dir         : Allow includes of files in directory\n" +
                     " --out (-o) dir             : Output directory (default: ${PWD})\n" +
                     " file                       : Files to compile.\n" +
                     "\n" +
                     "Available generators:\n" +
                     " - java       : Main java (1.8+) code generator.\n" +
                     " - tiny_java  : Minimalistic java (1.7+) code generator.\n" +
                     " - json       : Generates JSON specification files.\n",
                     outContent.toString());
        assertEquals("", errContent.toString());
        assertEquals(0, exitCode);
    }

    @Test
    public void testHelp_java() {
        compiler.run("--help", "java");

        assertEquals("Providence compiler - v" + version + "\n" +
                     "Usage: pvdc [-I dir] [-o dir] -g generator[:opt[,opt]*] file...\n" +
                     "\n" +
                     "java : Main java (1.8+) code generator.\n" +
                     "Available options\n" +
                     "\n" +
                     " - android : Add android parcelable interface to model classes.\n",
                     outContent.toString());
        assertEquals("", errContent.toString());
        assertEquals(0, exitCode);
    }

    @Test
    public void testHelp_tiny_java() {
        compiler.run("--help", "tiny_java");

        assertEquals("Providence compiler - v" + version + "\n" +
                     "Usage: pvdc [-I dir] [-o dir] -g generator[:opt[,opt]*] file...\n" +
                     "\n" +
                     "tiny_java : Minimalistic java (1.7+) code generator.\n" +
                     "Available options\n" +
                     "\n" +
                     " - jackson : Add jackson 2 annotations to model classes.\n",
                     outContent.toString());
        assertEquals("", errContent.toString());
        assertEquals(0, exitCode);
    }

    @Test
    public void testIncludeNotExist() {
        compiler.run("-I",
                     temp.getRoot().getAbsolutePath() + "/does_not_exist",
                     "-g", "java",
                     thriftFile.getAbsolutePath());

        assertEquals("", outContent.toString());
        assertEquals("Usage: pvdc [-I dir] [-o dir] -g generator[:opt[,opt]*] file...\n" +
                     "No such directory " + temp.getRoot().getAbsolutePath() + "/does_not_exist\n" +
                     "\n" +
                     "Run $ pvdc --help # for available options.\n", errContent.toString());
        assertEquals(1, exitCode);
    }

    @Test
    public void testIncludeNotADirectory() {
        compiler.run("-I",
                     thriftFile.getAbsolutePath(),
                     "-g", "java",
                     thriftFile.getAbsolutePath());

        assertEquals("", outContent.toString());
        assertEquals("Usage: pvdc [-I dir] [-o dir] -g generator[:opt[,opt]*] file...\n" +
                     "" + temp.getRoot().getAbsolutePath() + "/test.thrift is not a directory\n" +
                     "\n" +
                     "Run $ pvdc --help # for available options.\n", errContent.toString());
        assertEquals(1, exitCode);
    }

    @Test
    public void testOutputDirNotExist() {
        compiler.run("--out",
                     temp.getRoot().getAbsolutePath() + "/does_not_exist",
                     "-g", "java",
                     thriftFile.getAbsolutePath());

        assertEquals("", outContent.toString());
        assertEquals("Usage: pvdc [-I dir] [-o dir] -g generator[:opt[,opt]*] file...\n" +
                     "No such directory " + temp.getRoot().getAbsolutePath() + "/does_not_exist\n" +
                     "\n" +
                     "Run $ pvdc --help # for available options.\n", errContent.toString());
        assertEquals(1, exitCode);
    }

    @Test
    public void testOutputDirNotADirectory() {
        compiler.run("--out",
                     thriftFile.getAbsolutePath(),
                     "-g", "java",
                     thriftFile.getAbsolutePath());

        assertEquals("", outContent.toString());
        assertEquals("Usage: pvdc [-I dir] [-o dir] -g generator[:opt[,opt]*] file...\n" +
                     thriftFile.getAbsolutePath() + " is not a directory\n" +
                     "\n" +
                     "Run $ pvdc --help # for available options.\n", errContent.toString());
        assertEquals(1, exitCode);
    }

    @Test
    public void testCompile() {
        compiler.run("-I",
                     include.getAbsolutePath(),
                     "--out",
                     output.getAbsolutePath(),
                     "-g", "java",
                     thriftFile.getAbsolutePath());

        assertEquals("", outContent.toString());
        assertEquals("", errContent.toString());
        assertEquals(0, exitCode);

        // It generated the file in test.thrift.
        File service = new File(output, "net/morimekta/test/compiler/MyService.java");

        assertTrue(service.exists());
        assertTrue(service.isFile());

        // And not the one in ref.thrift
        File failure = new File(output, "net/morimekta/test/compiler_ref/Failure.java");

        assertFalse(failure.exists());
    }

    @Test
    public void testCompile_2() {
        compiler.run("--out",
                     output.getAbsolutePath(),
                     "-g", "java",
                     refFile.getAbsolutePath(),
                     thriftFile.getAbsolutePath());

        assertEquals("", outContent.toString());
        assertEquals("", errContent.toString());
        assertEquals(0, exitCode);

        // It generated the file in test.thrift.
        File service = new File(output, "net/morimekta/test/compiler/MyService.java");

        assertTrue(service.exists());
        assertTrue(service.isFile());

        // And not the one in ref.thrift
        File failure = new File(output, "net/morimekta/test/compiler_ref/Failure.java");

        assertTrue(failure.exists());
    }

    @Test
    public void testCompile_missingInclude() {
        compiler.run("--out",
                     output.getAbsolutePath(),
                     "-g", "java",
                     thriftFile.getAbsolutePath());

        assertEquals("", outContent.toString());
        assertEquals("No such package \"ref\" exists\n", errContent.toString());
        assertEquals(1, exitCode);
    }

    @Test
    public void testCompile_badReference() throws IOException {
        FileOutputStream file = new FileOutputStream(thriftFile);
        IOUtils.copy(getClass().getResourceAsStream("/compiler/test_2.thrift"), file);
        file.flush();
        file.close();

        compiler.run("-I",
                     include.getAbsolutePath(),
                     "--out",
                     output.getAbsolutePath(),
                     "-g", "java",
                     thriftFile.getAbsolutePath());

        assertEquals("", outContent.toString());
        assertEquals("No such type \"Request2\" in package \"ref\"\n", errContent.toString());
        assertEquals(1, exitCode);
    }
}
