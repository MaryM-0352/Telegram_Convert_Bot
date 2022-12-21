package src.main;

import org.telegram.telegrambots.meta.api.objects.PhotoSize;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DownFilePhoto {
    public static void DownFilePhoto(String fileName, String filePath) throws IOException {
        URL url = new URL("https://api.telegram.org/bot"+Bot.BOT_TOKEN+"/getFile?file_id="+filePath);
        String outFile = "upload_files/" + fileName + ".jpg";
        FileOutputStream out = null;
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(url.openStream());
            out = new FileOutputStream(outFile);
            byte[] data = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1){
                out.write(data, 0, count);
                out.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if (in != null){
                    in.close();
                }
            } finally {
                if(out != null){
                    out.close();
                }
            }
        }
    }

//херня, а не код


}
