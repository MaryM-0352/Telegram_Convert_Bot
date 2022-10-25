package src.main;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

//ДЕЛА НАСУЩНЫЕ: скачать картинку в норм форме(чтобы читалось)
public class Bot extends TelegramLongPollingBot{
    final static public String BOT_TOKEN = "5786660108:AAEkQ39HCI0htGNUhu5l_XXEmBRvirPJHTY";
    final private String BOT_NAME = "ConvertBot";

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }


    @Override
    public void onUpdateReceived(Update update) {
        //handle messages -> we look for the message we recieved

        Message message;
        if (update.hasMessage()) {
            if (update.hasCallbackQuery()) {
                String str = update.getCallbackQuery().getData();
            }
            //we save the message in new Message object
            message = update.getMessage();
            System.out.println(message);
            //we check that the text is not empty
            if (message != null && message.hasText()) {
                //we take the text, and convert it to lower case, so even if the user typed "/HELLO" it should work
                String msg = message.getText().toLowerCase();

                //now we tell the bot to respond to specific cases:
                if (msg.startsWith("/hello")) {
                    SendMsg(message, "Hello World!");
                } else if (msg.equals("/start")) {
                    SendMsg(message, "Ok! let's get the party started 😀\nWhat do you want to convert to PDF?");
                } else if (msg.startsWith("txt")){
                    SendMsg(message, "attach file with txt");
                }
            } else if (message.hasDocument()){
                try {
                    System.out.println("GOD");
                    var doc = update.getMessage().getDocument();
                    DownFileDocx(doc);
                    GetFilePDF(doc);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (message.hasPhoto()){
                try {
                    var doc = update.getMessage().getPhoto();
                    DownFilePhoto(doc);
                    //GetFilePDF(doc);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    private void DownFileDocx(Document doc) throws IOException {
        if (doc != null){
            final String fileId = doc.getFileId();
            final String fileName = doc.getFileName();
            UploadFile.uploadFile(fileName, fileId);
        }
    }

    private String ReadFile(Document document) throws IOException{
        //ЗАДАЧА: ДОКИ В TXT
        Path content_path = Paths.get("C:\\Users\\Maria\\IdeaProjects\\Bot\\src\\main\\resources\\upl_files\\", document.getFileName());
        BufferedReader reader = Files.newBufferedReader(content_path);
        String result = "";
        int c;
        while ((c = reader.read()) != -1) {
            result += (char) c;
        };

        return result;
    }
    private void DownFilePhoto(List<PhotoSize> doc) throws IOException {
        if (doc != null){
            final String fileId = doc.get(1).getFileId();
            final String fileName = doc.get(1).getFileUniqueId();
            UploadFile.uploadFile(fileName, fileId);
        }
    }

    private void GetFilePDF(Document document) throws IOException{
        Document document = new Document();
        try {
            // step 2:
            // we create a writer that listens to the document
            // and directs a PDF-stream to a file
            PdfWriter.getInstance(document,
                    new FileOutputStream("HelloWorld.pdf"));

            // step 3: we open the document
            document.open();
            // step 4: we add a paragraph to the document
            document.add(new Paragraph("Hello World"));
        } catch (DocumentException de) {
            System.err.println(de.getMessage());
        } catch (IOException ioe) {
            System.err.println(ioe.getMessage());
        }
        // step 5: we close the document
        document.close();

    }

        private void SendMsg (Message message, String s){
            SendMessage sendMessage = new SendMessage();

            //set the destination, and the text we want to send
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText(s);
            //try to send it:
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
