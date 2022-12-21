package src.main;

import org.telegram.telegrambots.meta.api.objects.Document;

import java.io.IOException;
import java.util.*;

public class CollectGroup {
    public static List<List<String>> collectGroup(List<String[]> dictionary) throws IOException {
        List<List<String>> all_text = new ArrayList<>();
        for (String[] info: dictionary){
            DownFileDocx.DownFileDocx(info[0], info[1]);
            all_text.add(ReadFile.ReadTXTFile(info[0]));
        }
        return all_text;
    }
}
