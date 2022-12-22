package src.main;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;

public class UploadFile {
    public static void uploadFile(String file_name, String file_id) throws IOException {
        System.out.println(file_name);
        URL url = new URL("https://api.telegram.org/bot"+Bot.BOT_TOKEN+"/getFile?file_id="+file_id);

        BufferedReader in = new BufferedReader(new InputStreamReader( url.openStream()));
        String res = in.readLine();

        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");

        File loc_file = new File("src/main/resources/upl_files/" + file_name);
        InputStream is = new URL("https://api.telegram.org/file/bot" + Bot.BOT_TOKEN + "/" + file_path).openStream();

        FileUtils.copyInputStreamToFile(is, loc_file);

        in.close();
        is.close();

    }

    public static void UploadPhoto(String fileName, String filePath) throws IOException {
        URL url = new URL("https://api.telegram.org/file/bot" + Bot.BOT_TOKEN + "/" + filePath);
        System.out.println(url);
        String outFile = "src/main/resources/upl_files/" + fileName + ".jpg";
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

}
