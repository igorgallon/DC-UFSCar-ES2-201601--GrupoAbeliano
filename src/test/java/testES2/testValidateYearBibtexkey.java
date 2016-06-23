package testES2;

import java.io.IOException;
import java.io.StringWriter;
import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.bibtex.BibEntryWriter;
import net.sf.jabref.exporter.LatexFieldFormatter;
import net.sf.jabref.model.database.BibDatabaseMode;
import net.sf.jabref.model.entry.BibEntry;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class testValidateYearBibtexkey {

    private BibEntryWriter writer;
    private static JabRefPreferences backup;

    @BeforeClass
    public static void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        backup = Globals.prefs;
    }

    @AfterClass
    public static void tearDown() {
        Globals.prefs.overwritePreferences(backup);
    }

    @Before
    public void setUpWriter() {
        writer = new BibEntryWriter(new LatexFieldFormatter(), true);
    }

    @Test
    public void testAnoValidoArticle() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "article");
        entry.setField("year", "2016");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE +
                "  year = {2016}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void testAnoValidoBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("year", "2016");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Book{," + Globals.NEWLINE +
                "  year = {2016}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    // Test works if the year still is 2016
    @Test(expected = IllegalArgumentException.class)
    public void testAnoFuturoArticle() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "article");
        entry.setField("year", "2017");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();
    }

    // Test works if the year still is 2016
    @Test(expected = IllegalArgumentException.class)
    public void testAnoFuturoBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("year", "2017");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnoInvalidoArticle() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "article");
        entry.setField("year", "-2015");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnoInvalidoArticleBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("year", "-2015");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnoComLetrasArticle() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "article");
        entry.setField("year", "A2015B");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAnoComLetrasBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "article");
        entry.setField("year", "A2015B");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();
    }

    @Test
    public void testAnoPreenchendoCamposArticle() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "article");
        //set a required field
        entry.setField("author", "Alexandre Tutui");
        entry.setField("year", "2016");
        //set an optional field
        entry.setField("volume", "1");
        entry.setField("number", "1");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE +
                "  author = {Alexandre Tutui}," + Globals.NEWLINE +
                "  year   = {2016}," + Globals.NEWLINE +
                "  volume = {1}," + Globals.NEWLINE +
                "  number = {1}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void testAnoPreenchendoCamposBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        //set a required field
        entry.setField("author", "Alexandre Jesus");
        entry.setField("year", "2013");
        //set an optional field
        entry.setField("volume", "2");
        entry.setField("number", "1");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Book{," + Globals.NEWLINE +
            "  year   = {2013}," + Globals.NEWLINE +
            "  author = {Alexandre Jesus}," + Globals.NEWLINE +
            "  volume = {2}," + Globals.NEWLINE +
            "  number = {1}," + Globals.NEWLINE +
            "}" + Globals.NEWLINE;

        assertEquals(expected, actual);
    }

    @Test
    public void testBibtexkeyValida() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("007", "book");
        entry.setCiteKey("a0");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);
        String actual = stringWriter.toString();

        String expected = Globals.NEWLINE + "@Book{"
                          + "a0,"
                          + Globals.NEWLINE + "}"
                + Globals.NEWLINE;

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBibtexkeyInvalidaNumero() throws IOException {
        BibEntry entry = new BibEntry("057", "book");
        // Uses a mock function to test, as the verification is
        // originally made in the gui
        entry.mockSetCiteKey("12");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBibtexkeyInvalidaTamanho() throws IOException {
        BibEntry entry = new BibEntry("07", "book");
        // Uses a mock function to test, as the verification is
        // originally made in the gui
        entry.mockSetCiteKey("a");
    }
}
