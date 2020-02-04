import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PDFReader {
    public static void main(String[] args) {
        ArrayList<String> pdfNames = getPDFDocumentNamesFromHTML();

        if(pdfNames.size() > 0) {
            String path = "https://dane.imgw.pl/data/current/ost_meteo/" + pdfNames.get(0);
            try {
                URL getPath = new URL(path);
                InputStream in = getPath.openStream();
                PDDocument document = PDDocument.load(in);
                if (!document.isEncrypted()) {
                    PDFTextStripper stripper = new PDFTextStripper();
                    String text = stripper.getText(document);
                    System.out.println("File name: " + pdfNames.get(0)); // .get(number_of_document)
                    System.out.println("Text:" + text);
                }
                document.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("There are currently no meteorological warnings.");
        }
    }

    public static ArrayList<String> getPDFDocumentNamesFromHTML(){
        ArrayList<String> pdfNames = new ArrayList<>();

        try {
            Document doc = Jsoup.connect("https://dane.imgw.pl/data/current/ost_meteo/").get();
            Elements links = doc.getElementsByTag("a");
            final String regex = ".+\\.pdf";
            Pattern pattern = Pattern.compile(regex);

            for (Element link : links) {
                Matcher matcher = pattern.matcher(link.text());
                if(matcher.matches()){
                    pdfNames.add(link.text());
                }
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }

        return pdfNames;
    }
}
