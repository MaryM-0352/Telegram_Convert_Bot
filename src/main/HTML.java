package src.main;

import com.spire.pdf.graphics.PdfMargins;
import com.spire.pdf.htmlconverter.LoadHtmlType;
import com.spire.pdf.htmlconverter.qt.HtmlConverter;
import com.spire.pdf.htmlconverter.qt.Size;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class HTML {
    public static void convertHTMLToPDF(String filename) throws IOException {
        String htmlString = HtmlToString("C:\\Users\\Maria\\Desktop\\test.html");
        System.out.println("we are here");
        //Specify the output file path
        String outputFile = "C:\\Users\\Maria\\HtmlToPdf.pdf";
        //Specify the plugin path
        String pluginPath = "C:\\plugins";
        //Set plugin path
        HtmlConverter.setPluginPath(pluginPath);
        //Convert HTML string to PDF
        HtmlConverter.convert(htmlString, outputFile, true, 100000, new Size(700, 900), new PdfMargins(0), LoadHtmlType.Source_Code);
        System.out.println("што");
    }

    public static String HtmlToString(String filePath) throws IOException {
        String path = filePath;
        File file = new File(path);
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String temp = "";
        while ((temp = bufferedReader.readLine()) != null) {
            stringBuilder.append(temp + "\n");
        }
        bufferedReader.close();
        String str = stringBuilder.toString();
        return str;
    }
}
