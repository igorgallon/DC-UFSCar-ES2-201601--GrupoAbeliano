
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

    // Padrão de arquivo csv encontrado na internet
    private static final Pattern CSV_PATTERN = Pattern.compile("(?<=\")([^\"]*)(?=\")|(?<=,|^)([^,]*)(?=,|$)");

    // Nome do formato que ira aparecer na caixa de dialogo
    @Override
    public String getFormatName() {
        return "CSV";
    }
    // Tipo de arquivo reconhecido
    @Override
    public String getCLIId() {
        return "csv";
    }

    @Override
    public boolean isRecognizedFormat(InputStream stream) throws IOException {

        // Carregando arquivo no buffer
        BufferedReader in = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(stream));

        // String para carregar um linha do arquivo
        String str;
        // Lista para carregar as entradas
        ArrayList<ArrayList<String>> entradas = new ArrayList<ArrayList<String>>();

        // Recebendo as entradas do arquivo, e colocando-as na lista entradas
        while ((str = in.readLine()) != null) {
            if (!CsvImporter.CSV_PATTERN.matcher(str).find()) {
                return false;
            }
            entradas.add(tratamentoString(str));
        }
        // Verifica se arquivo tem apenas cabecalho ou é vazio
        if (entradas.size() < 1) {
            return false;
        }
        // Verifica se todas as emtradas possuem o mesmo numero de campos
        for (int i = 0; i < (entradas.size() - 1); i++) {
            if (entradas.get(i).size() != entradas.get(i + 1).size()) {
                return false;
            }
        }
        // Caso o programa chegue ate esse ponto o arquivo é valido
        return true;
    }

    @Override
    public List<BibEntry> importEntries(InputStream stream, OutputPrinter status) throws IOException {

        // Variaveis usadas para coletar as entradas do arquivo e criar uma entrada BibEntry
        List<BibEntry> bibitems = new ArrayList<>();
        ArrayList<ArrayList<String>> entradas = new ArrayList<ArrayList<String>>();

        // Caso não tenha problemas na leitura do arquivo
        try (BufferedReader in = new BufferedReader(ImportFormatReader.getReaderDefaultEncoding(stream))) {
            String str;
            BibEntry novaEntrada = new BibEntry();

            // Lendo todas as linha do arquivo e colocando na lista entradas
            while ((str = in.readLine()) != null) {
                entradas.add(tratamentoString(str));
            }

            // TODO: Gera id unico local, pode causar problemas
            int id = 0;

            // Convertendo as entradas recebidas do arquivo para entradas BibEntry
            // Comeca em 1 porque a primeira posicao indica tipo da entrada
            for (int i = 1; i < entradas.size(); i++) {
                novaEntrada = new BibEntry(Integer.toString(id++) + DEFAULT_BIBTEXENTRY_ID, entradas.get(i).get(0));

                for (int j = 1; j < entradas.get(0).size(); j++) {
                novaEntrada.setField(entradas.get(0).get(j), entradas.get(i).get(j));
            }

               bibitems.add(novaEntrada);
            }
            // Retorna o resultado final
            return bibitems;
    }
    }

    public ArrayList<String> tratamentoString(String linha) {
        // Separadores de campos comuns e epeciais
        char[] separadores = {',', '\n'};
        char caracterEspecial = '"';

        // Lista contendo as linhas tratadas
        ArrayList<String> linhasTratadas = new ArrayList<>();

        // tamanho da linha recebida
        int tamanho = linha.length();
        int i = 0;

        // Passando por todos os caracteres buscando os separadores
        while (i < tamanho) {
            int proxSeparador = tamanho;
            // Buscando o proximo separador
            for (int j = 0; j < separadores.length; ++j) {
                int temp = linha.indexOf(separadores[j], i);
                if ((temp == -1) || (temp >= proxSeparador)) {
                    continue;
                }
                proxSeparador = temp;
            }

            // Colocando o separador especial no fim da linha por default
            int proxSeparadorEspecial = tamanho;

            // verificando se o caracter atual é o separador especial
            int temp = linha.indexOf(caracterEspecial, i);
            if ((temp == -1) || (temp >= proxSeparadorEspecial)) {
                proxSeparadorEspecial = tamanho;
            } else {
                proxSeparadorEspecial = temp;
            }

            // Caso o caracter atual seja o separador especial
            if (proxSeparadorEspecial == i) {
                char c = linha.charAt(i);
                // Busca pelo proximo separador especial para encontrar o campo
                int d = linha.indexOf((c + "") + (c + ""), i + 1);

                // Caso seja encontrado o proximo separador especial pular para a sua posicao
                int fim = linha.indexOf(c, d >= 0 ? d + 3 : i + 1);
                if (fim == -1) {
                    fim = tamanho;
                }
                // Colocando no campo a substring que esta entre os separadores especiais
                String campo = linha.substring(i + 1, fim);

                // Inserindo campo atual na lista de linhasTratadas
                campo = campo.replaceAll((c + "") + (c + ""), c + "");
                linhasTratadas.add(campo);
                i = fim + 1;
            }
            // Caso o caracter atual seja um separador ir para o proximo caracter
            else if (proxSeparador == i) {
                ++i;
            }
            // Caso contrario cortar o campo entra o caracter atual e o proximo separador
            else {
                linhasTratadas.add(linha.substring(i, proxSeparador));
                i = proxSeparador;
            }
        }
        // Retorna o resultado
        return linhasTratadas;
    }
}