package testES2;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;

import net.sf.jabref.Globals;
import net.sf.jabref.JabRefPreferences;
import net.sf.jabref.importer.ImportFormatReader;
import net.sf.jabref.importer.OutputPrinter;
import net.sf.jabref.importer.OutputPrinterToNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.junit.Assert.*;


@RunWith(Parameterized.class)
public class testImportFormatReader {

    private ImportFormatReader reader;

    private final String resourceName;
    private final int count;
    public final String format;
    private final String fileName;


    public testImportFormatReader(String resource, String format, int count) throws URISyntaxException {
        this.resourceName = resource;
        this.format = format;
        this.count = count;
        this.fileName = Paths.get(testImportFormatReader.class.getResource(resourceName).toURI()).toString();

    }

    @Before
    public void setUp() {
        Globals.prefs = JabRefPreferences.getInstance();
        reader = new ImportFormatReader();
        reader.resetImportFormats();
    }

    @Test
    public void testImportUnknownFormat() {
        ImportFormatReader.UnknownFormatImport unknownFormat = reader.importUnknownFormat(fileName);
        assertEquals(count, unknownFormat.parserResult.getDatabase().getEntryCount());
    }

    @Test
    public void testImportFormatFromFile() throws IOException {
        OutputPrinter nullPrinter = new OutputPrinterToNull();
        assertEquals(count, reader.importFromFile(format, fileName, nullPrinter).size());
    }

    @Parameterized.Parameters(name = "{index}: {1}")
    public static Collection<Object[]> importFormats() {
        Collection<Object[]> result = new ArrayList<>();
        result.add(new Object[] {"fileformat/RIS_Entry_1.ris", "ris", 1});
        result.add(new Object[] {"fileformat/RIS_Entry_4.ris", "ris", 4});
        result.add(new Object[] {"fileformat/RIS_Entry_Dup.ris", "ris", 4});
        result.add(new Object[] {"fileformat/BIB_Entry_1.bib", "bibtex", 1});
        result.add(new Object[] {"fileformat/BIB_Entry_4.bib", "bibtex", 4});
        result.add(new Object[] {"fileformat/BIB_Entry_Dup.bib", "bibtex", 4});
        result.add(new Object[] {"fileformat/ISI_Entry_1.isi", "isi", 1});
        result.add(new Object[] {"fileformat/TXT_Entry_1.txt", "refer", 1});
        result.add(new Object[] {"fileformat/TXT_Entry_4.txt", "refer", 4});
        result.add(new Object[] {"fileformat/TXT_Entry_Dup.txt", "refer", 4});

        return result;
    }
}


