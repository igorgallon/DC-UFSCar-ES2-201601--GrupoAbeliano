
package net.sf.jabref.importer.fileformat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import net.sf.jabref.importer.ImportFormatReader;
import net.sf.jabref.importer.OutputPrinter;
import net.sf.jabref.model.entry.BibEntry;


public class CsvImporter extends ImportFormat {

    // Pode ser desnescessauro
    private static final Pattern CSV_PATTERN = Pattern.compile("((\"([^\"]*),+([^\"]*)\")||([^\"]*),([^\"]*))\n?");


    @Override
    public String getFormatName() {
        return "CSV file";
    }

    @Override
    public String getCLIId() {
        return "csv";
    }



    public ArrayList<String> tratamentoString(String linha) {

        char[] separadores = {',', '\n'};
        char caracterEspecial = '"';

        /** Using ArrayList as the number of values are unknown at this stage */
        ArrayList<String> linhasTratadas = new ArrayList<>();

        int tamanho = linha.length();
        int i = 0;

        /** iterate through all the chars in the line */
        while (i < tamanho) {
            int proxSeparador = tamanho;
            /** Get the next separator */
            for (int j = 0; j < separadores.length; ++j) {
                int temp = linha.indexOf(separadores[j], i);
                if ((temp == -1) || (temp >= proxSeparador)) {
                    continue;
                }
                proxSeparador = temp;
            }

            /** Place the special separator at the end of the string */
            int proxSeparadorEspecial = tamanho;

            /** Check if there is any special separator */
            int temp = linha.indexOf(caracterEspecial, i);
            if ((temp == -1) || (temp >= proxSeparadorEspecial)) {
                proxSeparadorEspecial = tamanho;
            } else {
                proxSeparadorEspecial = temp;
            }

            /** if we are at the special separator get the text until the next special separator */
            if (proxSeparadorEspecial == i) {
                char c = linha.charAt(i);
                /** check if there is any double quote chars in the text */
                int d = linha.indexOf((c + "") + (c + ""), i + 1);

                /** if there are two double quota chars jump to the next one - are part of the text */
                int fim = linha.indexOf(c, d >= 0 ? d + 3 : i + 1);
                if (fim == -1) {
                    fim = tamanho;
                }
                String campo = linha.substring(i + 1, fim);
                /** Replace two double quota with one double quota */
                campo = campo.replaceAll((c + "") + (c + ""), c + "");

                linhasTratadas.add(campo);
                i = fim + 1;
            }
            /** if we are at a normal separator, ignore the separator and jump to the next char */
            else if (proxSeparador == i) {
                ++i;
            }
            /** Copy the value in the result string */
            else {
                linhasTratadas.add(linha.substring(i, proxSeparador));
                i = proxSeparador;
            }
        }

        /** Convert the result to String[] */
        return linhasTratadas;
    }

    @Override
    public boolean isRecognizedFormat(InputStream stream) throws IOException {

        try (BufferedReader in = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(stream))) {

            String str;
        boolean status = false;
        int oldpieces = -1, newpieces;
        ArrayList<String> aux;

        while ((str = in.readLine()) != null) {
            aux = tratamentoString(str);
            newpieces = aux.size();
            if (oldpieces != -1) {
                if (((oldpieces != newpieces) && (newpieces > 0)) || !CsvImporter.CSV_PATTERN.matcher(str).find()) {
                    return false;
                }
            } else if (!CsvImporter.CSV_PATTERN.matcher(str).find()) {
                return false;
            }

            oldpieces = newpieces;
            //for now its true
            status = true;
        }
        return status;
        }
    }


    @Override
    public List<BibEntry> importEntries(InputStream stream, OutputPrinter status) throws IOException {
        if (stream == null) {
            throw new IOException("No stream given.");
        }

        List<BibEntry> bibitems = new ArrayList<>();
        String[][] entradas = new String[4][4];
        String[] aux = new String[4];
        ArrayList<String> campos = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(stream))) {
            String str;
            int i = 0;
            while ((str = in.readLine()) != null) {
                campos.clear();
                campos = tratamentoString(str);

                int tam = campos.size();
                for (int j = 0; j < tam; j++) {
                    aux[j] = campos.get(j);
                }
                entradas[i] = aux;
                i++;
            }
            BibEntry novaEntrada = new BibEntry();
            for (String[] entradaAtual : entradas) {
                if (entradaAtual != entradas[0]) {

                    novaEntrada = new BibEntry(DEFAULT_BIBTEXENTRY_ID, entradaAtual[0]);

                    for (int k = 1; k < entradas[0].length; k++) {
                        novaEntrada.setField(entradas[0][k], entradaAtual[k]);
                        System.out.print(entradas[0][k] + "=" + entradaAtual[k]);
                    }
                }
                System.out.println(novaEntrada);
                bibitems.add(novaEntrada);
            }
        }
        return bibitems;
    }
}
