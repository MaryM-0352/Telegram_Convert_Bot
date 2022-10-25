package src.main;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;

public class UploadFile {
    public static void uploadFile(String file_name, String file_id) throws IOException {
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

}
