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

public class testBibtexEntry {

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

    // Teste para ARTICLES

    @Test
    public void testTodosCamposVaziosArticle() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0001", "article");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void testTodosCamposPreenchidosArticle() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "article");
        //set a required field
        entry.setField("author", "Alexandre Tutui");
        entry.setField("title", "Peripécias escolares");
        entry.setField("journal", "Jornal da Escola");
        entry.setField("year", "2016");
        //set an optional field
        entry.setField("volume", "1");
        entry.setField("number", "1");
        entry.setField("pages", "120");
        entry.setField("month", "1");
        entry.setField("note", "Este livro é muito bom!");
        entry.setField("abstract", "Peripécias escolares do Tutui");
        entry.setField("comment", "Muito bom!");
        entry.setField("crossref", "Peripécias acadêmicas");
        entry.setField("doi", "1234567890");
        entry.setField("keywords", "Peripecias");
        entry.setField("owner", "Alexandre");
        entry.setField("review", "Muito bom!");
        entry.setField("timestamp", "2009");
        entry.setField("url", "www.url.com.br");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE +
                "  author    = {Alexandre Tutui}," + Globals.NEWLINE +
                "  title     = {Peripécias escolares}," + Globals.NEWLINE +
                "  journal   = {Jornal da Escola}," + Globals.NEWLINE +
                "  year      = {2016}," + Globals.NEWLINE +
                "  volume    = {1}," + Globals.NEWLINE +
                "  number    = {1}," + Globals.NEWLINE +
                "  pages     = {120}," + Globals.NEWLINE +
                "  month     = {1}," + Globals.NEWLINE +
                "  note      = {Este livro é muito bom!}," + Globals.NEWLINE +
                "  abstract  = {Peripécias escolares do Tutui}," + Globals.NEWLINE +
                "  comment   = {Muito bom!}," + Globals.NEWLINE +
                "  crossref  = {Peripécias acadêmicas}," + Globals.NEWLINE +
                "  doi       = {1234567890}," + Globals.NEWLINE +
                "  keywords  = {Peripecias}," + Globals.NEWLINE +
                "  owner     = {Alexandre}," + Globals.NEWLINE +
                "  review    = {Muito bom!}," + Globals.NEWLINE +
                "  timestamp = {2009}," + Globals.NEWLINE +
                "  url       = {www.url.com.br}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void testCamposObrigatoriosPreenchidosArticle() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0003", "article");
        //set a required field
        entry.setField("author", "Victor Pão");
        entry.setField("title", "Como ser um bom garoto");
        entry.setField("journal", "Publicação autônoma");
        entry.setField("year", "2015");
        //set an optional field

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE +
                "  author  = {Victor Pão}," + Globals.NEWLINE +
                "  title   = {Como ser um bom garoto}," + Globals.NEWLINE +
                "  journal = {Publicação autônoma}," + Globals.NEWLINE +
                "  year    = {2015}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void testCamposOpcionaisPreenchidosArticle() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0004", "article");
        //set an optional field
        entry.setField("volume", "2");
        entry.setField("pages", "1053");
        entry.setField("note", "Nenhuma nota");
        entry.setField("number", "3");
        entry.setField("month", "9");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE +
                "  volume = {2}," + Globals.NEWLINE +
                "  number = {3}," + Globals.NEWLINE +
                "  pages  = {1053}," + Globals.NEWLINE +
                "  month  = {9}," + Globals.NEWLINE +
                "  note   = {Nenhuma nota}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void testCamposOutrosPreenchidosArticle() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0005", "article");
        entry.setField("abstract", "Programa de humor do SBT");
        entry.setField("comment", "Melhor que Zorra Total");
        entry.setField("crossref", "Zorra Total");
        entry.setField("doi", "9876543210");
        entry.setField("keywords", "Praça");
        entry.setField("owner", "Carlos Alberto de Nóbrega");
        entry.setField("review", "Humor para toda família");
        entry.setField("timestamp", "1997");
        entry.setField("url", "www.praçódia.com.br");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE +
                "  abstract  = {Programa de humor do SBT}," + Globals.NEWLINE +
                "  comment   = {Melhor que Zorra Total}," + Globals.NEWLINE +
                "  crossref  = {Zorra Total}," + Globals.NEWLINE +
                "  doi       = {9876543210}," + Globals.NEWLINE +
                "  keywords  = {Praça}," + Globals.NEWLINE +
                "  owner     = {Carlos Alberto de Nóbrega}," + Globals.NEWLINE +
                "  review    = {Humor para toda família}," + Globals.NEWLINE +
                "  timestamp = {1997}," + Globals.NEWLINE +
                "  url       = {www.praçódia.com.br}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void testUmDeCadaCampoArticle() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0006", "article");
        entry.setField("author", "Furacão 2000");
        entry.setField("volume", "2");
        entry.setField("abstract", "Grandes sucessos de 2000");
        entry.setField("comment", "Melhor coletânea de 2000");
        entry.setField("review", "CD muito bem produzido");
        entry.setField("url", "www.furacao2000.com.br");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Article{," + Globals.NEWLINE +
                "  author   = {Furacão 2000}," + Globals.NEWLINE +
                "  volume   = {2}," + Globals.NEWLINE +
                "  abstract = {Grandes sucessos de 2000}," + Globals.NEWLINE +
                "  comment  = {Melhor coletânea de 2000}," + Globals.NEWLINE +
                "  review   = {CD muito bem produzido}," + Globals.NEWLINE +
                "  url      = {www.furacao2000.com.br}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    // Teste para BOOKS

    @Test
    public void testTodosCamposVaziosBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0001", "book");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Book{," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void testTodosCamposOpcionaisPreenchidosBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        // Campo obrigatorio
        entry.setField("title", "Alice no pais das maravilhas");
        entry.setField("publisher", "Alice");
        entry.setField("year", "1900");
        entry.setField("author", "Alice");
        entry.setField("editor", "Nenhum");
        // Campo opcional
        entry.setField("volume", "1");
        entry.setField("number", "1");
        entry.setField("series", "1");
        entry.setField("address", "Washington Luiz km 235");
        entry.setField("edition", "1");
        entry.setField("month", "1");
        entry.setField("note", "10");

        entry.setField("abstract", "Aventuras de Alice");

        entry.setField("comment", "Muito bom!");
        entry.setField("crossref", "Branca de Neve");
        entry.setField("doi", "1111111111");
        entry.setField("keywords", "Alice, Maravilhas");
        entry.setField("owner", "Tutui");
        entry.setField("review", "Muito bom!");

        entry.setField("url", "www.alice.com.br");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Book{," + Globals.NEWLINE +
                "  title     = {Alice no pais das maravilhas}," + Globals.NEWLINE +
                "  publisher = {Alice}," + Globals.NEWLINE +
                "  year      = {1900}," + Globals.NEWLINE +
                "  author    = {Alice}," + Globals.NEWLINE +
                "  editor    = {Nenhum}," + Globals.NEWLINE +
                "  volume    = {1}," + Globals.NEWLINE +
                "  number    = {1}," + Globals.NEWLINE +
                "  series    = {1}," + Globals.NEWLINE +
                "  address   = {Washington Luiz km 235}," + Globals.NEWLINE +
                "  edition   = {1}," + Globals.NEWLINE +
                "  month     = {1}," + Globals.NEWLINE +
                "  note      = {10}," + Globals.NEWLINE +
                "  abstract  = {Aventuras de Alice}," + Globals.NEWLINE +
                "  comment   = {Muito bom!}," + Globals.NEWLINE +
                "  crossref  = {Branca de Neve}," + Globals.NEWLINE +
                "  doi       = {1111111111}," + Globals.NEWLINE +
                "  keywords  = {Alice, Maravilhas}," + Globals.NEWLINE +
                "  owner     = {Tutui}," + Globals.NEWLINE +
                "  review    = {Muito bom!}," + Globals.NEWLINE +
                "  url       = {www.alice.com.br}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void testCamposObrigatoriosPreenchidosBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0003", "book");
        // Campo obrigatorio
        entry.setField("title", "Cavaleiros da Tavola Redonda");
        entry.setField("publisher", "Rei Arthur");
        entry.setField("year", "1800");
        entry.setField("author", "Rei Arthur");
        entry.setField("editor", "Varios editores");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Book{," + Globals.NEWLINE +
                "  title     = {Cavaleiros da Tavola Redonda}," + Globals.NEWLINE +
                "  publisher = {Rei Arthur}," + Globals.NEWLINE +
                "  year      = {1800}," + Globals.NEWLINE +
                "  author    = {Rei Arthur}," + Globals.NEWLINE +
                "  editor    = {Varios editores}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void testCamposOpcionaisPreenchidosBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0004", "book");
        // Campo opcional
        entry.setField("volume", "3");
        entry.setField("number", "2");
        entry.setField("series", "1");
        entry.setField("address", "Rua dos Bobos, 0");
        entry.setField("edition", "8");
        entry.setField("month", "5");
        entry.setField("note", "Livro muito legal");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Book{," + Globals.NEWLINE +
                "  volume  = {3}," + Globals.NEWLINE +
                "  number  = {2}," + Globals.NEWLINE +
                "  series  = {1}," + Globals.NEWLINE +
                "  address = {Rua dos Bobos, 0}," + Globals.NEWLINE +
                "  edition = {8}," + Globals.NEWLINE +
                "  month   = {5}," + Globals.NEWLINE +
                "  note    = {Livro muito legal}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void testCamposOutrosPreenchidosBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0005", "book");
        entry.setField("abstract", "Aventuras de Bentinho");
        entry.setField("comment", "Top demais!");
        entry.setField("crossref", "Fuvest");
        entry.setField("doi", "2222222222");
        entry.setField("keywords", "Memórias, Milícias");
        entry.setField("owner", "Tutui");
        entry.setField("review", "Muito bom!");
        entry.setField("url", "www.msm.com.br");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Book{," + Globals.NEWLINE +
                "  abstract = {Aventuras de Bentinho}," + Globals.NEWLINE +
                "  comment  = {Top demais!}," + Globals.NEWLINE +
                "  crossref = {Fuvest}," + Globals.NEWLINE +
                "  doi      = {2222222222}," + Globals.NEWLINE +
                "  keywords = {Memórias, Milícias}," + Globals.NEWLINE +
                "  owner    = {Tutui}," + Globals.NEWLINE +
                "  review   = {Muito bom!}," + Globals.NEWLINE +
                "  url      = {www.msm.com.br}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

    @Test
    public void testTodosCamposOpcionaisPreenchidosBook() throws IOException {
        StringWriter stringWriter = new StringWriter();

        BibEntry entry = new BibEntry("0002", "book");
        // Campo obrigatorio
        entry.setField("title", "Calculo I");
        // Campo opcional
        entry.setField("volume", "1");
        entry.setField("abstract", "Calculo diferencial");
        entry.setField("comment", "Muito bom!");
        entry.setField("review", "Muito bom!");
        entry.setField("url", "www.calc1.com.br");

        writer.write(entry, stringWriter, BibDatabaseMode.BIBTEX);

        String actual = stringWriter.toString();

        // @formatter:off
        String expected = Globals.NEWLINE + "@Book{," + Globals.NEWLINE +
                "  title    = {Calculo I}," + Globals.NEWLINE +
                "  volume   = {1}," + Globals.NEWLINE +
                "  abstract = {Calculo diferencial}," + Globals.NEWLINE +
                "  comment  = {Muito bom!}," + Globals.NEWLINE +
                "  review   = {Muito bom!}," + Globals.NEWLINE +
                "  url      = {www.calc1.com.br}," + Globals.NEWLINE +
                "}" + Globals.NEWLINE;
        // @formatter:on

        assertEquals(expected, actual);
    }

}
