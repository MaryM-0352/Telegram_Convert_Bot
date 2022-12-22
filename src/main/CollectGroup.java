package src.main;

import java.io.IOException;
import java.util.*;

public class CollectGroup {
    public static List<List<String>> collectFileGroup(List<String[]> dictionary) throws IOException {
        List<List<String>> all_text = new ArrayList<>();
        for (String[] info: dictionary){
            UploadFile.uploadFile(info[0], info[1]);
            System.out.println("It is upload");
            all_text.add(ReadFile.ReadTXTFile(info[0]));
        }
        return all_text;
    }
}
