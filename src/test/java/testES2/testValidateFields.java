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

public class testValidateFields {

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
    public void testValidYearArticle() throws IOException {
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
    public void testValidYearBook() throws IOException {
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
    public void testFutureYearArticle() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "article");
        entry.setField("year", "2017");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);
    }

    // Test works if the year still is 2016
    @Test(expected = IllegalArgumentException.class)
    public void testFutureYearBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("year", "2017");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidYearBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "article");
        entry.setField("year", "-2015");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidYearArticleBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("year", "-2015");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testYearWithLettersArticle() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "article");
        entry.setField("year", "A2015B");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testYearWithLettersBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "article");
        entry.setField("year", "A2015B");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);
    }

    @Test
    public void testAllValidatedFieldsArticle() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "article");
        //set a required field
        entry.setField("bibtexkey", "B5");
        entry.setField("author", "Alexandre Jesus");
        entry.setField("year", "2013");
        entry.setField("editor", "Lucileide");
        //set an optional field
        entry.setField("month", "Março");
        entry.setField("number", "1");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Article{B5," + Globals.NEWLINE +
                "  author = {Alexandre Jesus}," + Globals.NEWLINE +
                "  year   = {2013}," + Globals.NEWLINE +
                "  number = {1}," + Globals.NEWLINE +
                "  month  = {Março}," + Globals.NEWLINE +
                "  editor = {Lucileide}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;

        assertEquals(expected, actual);
    }

    @Test
    public void testAllValidatedFieldsBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        //set a required field
        entry.setField("bibtexkey", "J8");
        entry.setField("author", "Alexandre Jesus");
        entry.setField("year", "2013");
        entry.setField("editor", "Lucileide");
        //set an optional field
        entry.setField("month", "Março");
        entry.setField("number", "1");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Book{J8," + Globals.NEWLINE +
            "  year   = {2013}," + Globals.NEWLINE +
            "  author = {Alexandre Jesus}," + Globals.NEWLINE +
            "  editor = {Lucileide}," + Globals.NEWLINE +
            "  number = {1}," + Globals.NEWLINE +
            "  month  = {Março}," + Globals.NEWLINE +
            "}" + Globals.NEWLINE;

        assertEquals(expected, actual);
    }

    @Test
    public void testValidBibtexkey() throws IOException {
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
    public void testInvalidBibtexkeyNumber() throws IOException {
        BibEntry entry = new BibEntry("057", "book");
        // Uses a mock function to test, as the verification is
        // originally made in the gui
        entry.mockSetCiteKey("12");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBibtexkeySize() throws IOException {
        BibEntry entry = new BibEntry("07", "book");
        // Uses a mock function to test, as the verification is
        // originally made in the gui
        entry.mockSetCiteKey("a");
    }

    @Test
    public void testValidAuthor() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("author", "Juan J. Jorge");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Book{," + Globals.NEWLINE +
                "  author = {Juan J. Jorge}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidAuthorWithNumbers() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("author", "Juan 3 Jorge");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        stringWriter.toString();
    }

    @Test
    public void testValidEditor() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("editor", "Juan J. Jorge");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Book{," + Globals.NEWLINE +
                "  editor = {Juan J. Jorge}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEditorWithNumbers() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("editor", "Juan 3 Jorge");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        stringWriter.toString();
    }

    @Test
    public void testValidMonthName() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("month", "september");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Book{," + Globals.NEWLINE +
                "  month = {september}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void testValidMonthName2() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "journal");
        entry.setField("month", "ABriL");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Journal{," + Globals.NEWLINE +
                "  month = {ABriL}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void testValidMonthNumber() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("month", "0003");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Book{," + Globals.NEWLINE +
                "  month = {0003}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMonthWithNumbersLower() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("month", "0");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        stringWriter.toString();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMonthWithNumbers() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("month", "13");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        stringWriter.toString();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMonthName() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("month", "Janneiru");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        stringWriter.toString();
    }

    @Test
    public void testValidNumberField() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("number", "00489");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);
        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Book{," + Globals.NEWLINE +
                "  number = {00489}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidNumberField() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        entry.setField("number", ".00489");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);
        stringWriter.toString();
    }
}
