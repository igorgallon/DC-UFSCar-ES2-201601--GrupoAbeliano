package testES2;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import net.sf.jabref.BibDatabaseContext;
import net.sf.jabref.Defaults;
import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.MetaData;
import net.sf.jabref.bibtex.BibEntryWriter;

import org.junit.Before;
import org.junit.Test;
import net.sf.jabref.exporter.BibDatabaseWriter;
import net.sf.jabref.exporter.LatexFieldFormatter;
import net.sf.jabref.exporter.SavePreferences;
import net.sf.jabref.model.database.BibDatabase;
import net.sf.jabref.model.database.BibDatabaseMode;
import net.sf.jabref.model.entry.BibEntry;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import static org.junit.Assert.assertEquals;
import java.util.List;

import net.sf.jabref.importer.ImportFormatReader;
import net.sf.jabref.importer.OutputPrinter;
import net.sf.jabref.importer.OutputPrinterToNull;

// -----------
public class ImportToDatabaseTest {

    private ImportFormatReader reader;
    private BibEntryWriter writer;
    private static JabRefPreferences backup;

    private BibDatabaseWriter databaseWriter;
    private StringWriter stringWriter;
    private BibDatabase database;
    private MetaData metaData;
    private BibDatabaseContext bibtexContext;

    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        reader = new ImportFormatReader();
        reader.resetImportFormats();

        writer = new BibEntryWriter(new LatexFieldFormatter(), true);

        databaseWriter = new BibDatabaseWriter();
        stringWriter = new StringWriter();
        database = new BibDatabase();
        metaData = new MetaData();
        bibtexContext = new BibDatabaseContext(database, metaData, new Defaults(BibDatabaseMode.BIBTEX));
    }

    @BeforeClass
    public static void setUpBeforeClass() {
        Globals.prefs = JabRefPreferences.getInstance();
        backup = Globals.prefs;
    }

    @AfterClass
    public static void tearDown() {
        Globals.prefs.overwritePreferences(backup);
    }

    @Test
    public void importToExistantDatabase() throws IOException, URISyntaxException {
        BibEntry entry = new BibEntry("007", "article");
        entry.setField("bibtexkey", "ab");
        entry.setField("author", "Victor Pão");
        entry.setField("title", "Como ser um bom garoto");
        entry.setField("journal", "Publicação autônoma");
        entry.setField("year", "2015");

        database.insertEntry(entry);
        OutputPrinter nullPrinter = new OutputPrinterToNull();

        String resourceName = "fileformat/BIB_Entry_1.bib";
        String format = "bibtex";
        int count = 1;
        String fileName = Paths.get(ImportToDatabaseTest.class.getResource(resourceName).toURI()).toString();

        List<BibEntry> entryList = reader.importFromFile(format, fileName, nullPrinter);
        if (entryList.size() != 0) {
            for (int i = 0; i < entryList.size(); i++) {
                database.insertEntry(entryList.get(i));
            }
        }

        databaseWriter.writePartOfDatabase(stringWriter, bibtexContext, database.getEntries(),
                new SavePreferences());

        String expected = Globals.NEWLINE + "@Article{A1234," + Globals.NEWLINE
                + "  author   = {Tutui}," + Globals.NEWLINE
                + "  title    = {Contos do Tutui}," + Globals.NEWLINE
                + "  journal  = {EnC}," + Globals.NEWLINE
                + "  year     = {2016}," + Globals.NEWLINE
                + "  volume   = {1}," + Globals.NEWLINE
                + "  number   = {2}," + Globals.NEWLINE
                + "  pages    = {10}," + Globals.NEWLINE
                + "  month    = {4}," + Globals.NEWLINE + "  note     = {5}," + Globals.NEWLINE
                + "  abstract = {Nenhum abstract}," + Globals.NEWLINE
                + "  review   = {Otimo!}," + Globals.NEWLINE
                + "}" + Globals.NEWLINE + Globals.NEWLINE
                + "@Article{ab," + Globals.NEWLINE
                + "  author  = {Victor Pão}," + Globals.NEWLINE
                + "  title   = {Como ser um bom garoto}," + Globals.NEWLINE
                + "  journal = {Publicação autônoma}," + Globals.NEWLINE
                + "  year    = {2015}," + Globals.NEWLINE
                + "}" + Globals.NEWLINE + Globals.NEWLINE
                + "@Comment{jabref-meta: databaseType:bibtex;}" + Globals.NEWLINE;

        assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void importToEmptyDatabase() throws IOException, URISyntaxException {
        OutputPrinter nullPrinter = new OutputPrinterToNull();

        String resourceName = "fileformat/BIB_Entry_1.bib";
        String format = "bibtex";
        int count = 1;
        String fileName = Paths.get(ImportToDatabaseTest.class.getResource(resourceName).toURI()).toString();

        List<BibEntry> entryList = reader.importFromFile(format, fileName, nullPrinter);
        if (entryList.size() != 0) {
            for (int i = 0; i < entryList.size(); i++) {
                database.insertEntry(entryList.get(i));
            }
        }

        databaseWriter.writePartOfDatabase(stringWriter, bibtexContext, database.getEntries(), new SavePreferences());

        String expected = Globals.NEWLINE + "@Article{A1234," + Globals.NEWLINE + "  author   = {Tutui},"
                + Globals.NEWLINE + "  title    = {Contos do Tutui}," + Globals.NEWLINE + "  journal  = {EnC},"
                + Globals.NEWLINE + "  year     = {2016}," + Globals.NEWLINE + "  volume   = {1}," + Globals.NEWLINE
                + "  number   = {2}," + Globals.NEWLINE + "  pages    = {10}," + Globals.NEWLINE + "  month    = {4},"
                + Globals.NEWLINE + "  note     = {5}," + Globals.NEWLINE + "  abstract = {Nenhum abstract},"
                + Globals.NEWLINE + "  review   = {Otimo!}," + Globals.NEWLINE + "}" + Globals.NEWLINE + Globals.NEWLINE
                + "@Comment{jabref-meta: databaseType:bibtex;}" + Globals.NEWLINE;

        assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void importWrongFileToExistentDatabase() throws IOException, URISyntaxException {
        BibEntry entry = new BibEntry("007", "article");
        entry.setField("bibtexkey", "ab");
        entry.setField("author", "Victor Pão");
        entry.setField("title", "Como ser um bom garoto");
        entry.setField("journal", "Publicação autônoma");
        entry.setField("year", "2015");

        database.insertEntry(entry);
        OutputPrinter nullPrinter = new OutputPrinterToNull();

        String resourceName = "fileformat/BIB_Entry_Wrong.bib";
        String format = "bibtex";
        int count = 1;
        String fileName = Paths.get(ImportToDatabaseTest.class.getResource(resourceName).toURI()).toString();

        List<BibEntry> entryList = reader.importFromFile(format, fileName, nullPrinter);
        if (entryList.size() != 0) {
            for (int i = 0; i < entryList.size(); i++) {
                database.insertEntry(entryList.get(i));
            }
        }

        databaseWriter.writePartOfDatabase(stringWriter, bibtexContext, database.getEntries(), new SavePreferences());

        String expected = Globals.NEWLINE + "@Article{ab," + Globals.NEWLINE + "  author  = {Victor Pão},"
                + Globals.NEWLINE + "  title   = {Como ser um bom garoto}," + Globals.NEWLINE
                + "  journal = {Publicação autônoma}," + Globals.NEWLINE + "  year    = {2015}," + Globals.NEWLINE + "}"
                + Globals.NEWLINE + Globals.NEWLINE + "@Comment{jabref-meta: databaseType:bibtex;}" + Globals.NEWLINE;

        assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void importFileToExistentDatabaseCSV() throws IOException, URISyntaxException {
        BibEntry entry = new BibEntry("007", "article");
        entry.setField("bibtexkey", "ab");
        entry.setField("author", "Victor Pão");
        entry.setField("title", "Como ser um bom garoto");
        entry.setField("journal", "Publicação autônoma");
        entry.setField("year", "2015");

        database.insertEntry(entry);
        OutputPrinter nullPrinter = new OutputPrinterToNull();

        String resourceName = "fileformat/CSV_Entry_2.csv";
        String format = "csv";
        String fileName = Paths.get(ImportToDatabaseTest.class.getResource(resourceName).toURI()).toString();

        List<BibEntry> entryList = reader.importFromFile(format, fileName, nullPrinter);
        if (entryList.size() != 0) {
            for (int i = 0; i < entryList.size(); i++) {
                database.insertEntry(entryList.get(i));
            }
        }

        databaseWriter.writePartOfDatabase(stringWriter, bibtexContext, database.getEntries(),
                new SavePreferences());

        String expected = Globals.NEWLINE
                + "@Article{ab," + Globals.NEWLINE
                + "  author  = {Victor Pão}," + Globals.NEWLINE
                + "  title   = {Como ser um bom garoto}," + Globals.NEWLINE
                + "  journal = {Publicação autônoma}," + Globals.NEWLINE
                + "  year    = {2015}," + Globals.NEWLINE
                + "}" + Globals.NEWLINE + Globals.NEWLINE
                + "@Article{," + Globals.NEWLINE
                + "  author = {Tutui}," + Globals.NEWLINE + "}" + Globals.NEWLINE
                + Globals.NEWLINE
                + "@Book{," + Globals.NEWLINE + "  author = {Joao}," + Globals.NEWLINE + "}" + Globals.NEWLINE
                + Globals.NEWLINE
                + "@Comment{jabref-meta: databaseType:bibtex;}" + Globals.NEWLINE;

        assertEquals(expected, stringWriter.toString());
    }

    @Test
    public void importWrongFileToExistentDatabaseCSV() throws IOException, URISyntaxException {
        BibEntry entry = new BibEntry("007", "article");
        entry.setField("bibtexkey", "ab");
        entry.setField("author", "Victor Pão");
        entry.setField("title", "Como ser um bom garoto");
        entry.setField("journal", "Publicação autônoma");
        entry.setField("year", "2015");

        database.insertEntry(entry);
        OutputPrinter nullPrinter = new OutputPrinterToNull();

        String resourceName = "fileformat/CSV_Entry_Wrong.csv";
        String format = "bibtex";
        int count = 1;
        String fileName = Paths.get(ImportToDatabaseTest.class.getResource(resourceName).toURI()).toString();

        List<BibEntry> entryList = reader.importFromFile(format, fileName, nullPrinter);
        if (entryList.size() != 0) {
            for (int i = 0; i < entryList.size(); i++) {
                database.insertEntry(entryList.get(i));
            }
        }

        databaseWriter.writePartOfDatabase(stringWriter, bibtexContext, database.getEntries(), new SavePreferences());

        String expected = Globals.NEWLINE + "@Article{ab," + Globals.NEWLINE + "  author  = {Victor Pão},"
                + Globals.NEWLINE + "  title   = {Como ser um bom garoto}," + Globals.NEWLINE
                + "  journal = {Publicação autônoma}," + Globals.NEWLINE + "  year    = {2015}," + Globals.NEWLINE + "}"
                + Globals.NEWLINE + Globals.NEWLINE + "@Comment{jabref-meta: databaseType:bibtex;}" + Globals.NEWLINE;

        assertEquals(expected, stringWriter.toString());
    }
    
    @Test
    public void importToEmptyDatabaseCSV() throws IOException, URISyntaxException {
        OutputPrinter nullPrinter = new OutputPrinterToNull();

        String resourceName = "fileformat/CSV_Entry_2.csv";
        String format = "csv";
        int count = 1;
        String fileName = Paths.get(ImportToDatabaseTest.class.getResource(resourceName).toURI()).toString();

        List<BibEntry> entryList = reader.importFromFile(format, fileName, nullPrinter);
        if (entryList.size() != 0) {
            for (int i = 0; i < entryList.size(); i++) {
                database.insertEntry(entryList.get(i));
            }
        }
        
        databaseWriter.writePartOfDatabase(stringWriter, bibtexContext, database.getEntries(),
                new SavePreferences());

        String expected = Globals.NEWLINE
                + "@Article{," + Globals.NEWLINE
                + "  author = {Tutui}," + Globals.NEWLINE + "}" + Globals.NEWLINE
                + Globals.NEWLINE
                + "@Book{," + Globals.NEWLINE + "  author = {Joao}," + Globals.NEWLINE + "}" + Globals.NEWLINE
                + Globals.NEWLINE
                + "@Comment{jabref-meta: databaseType:bibtex;}" + Globals.NEWLINE;

        assertEquals(expected, stringWriter.toString());
    }
}
