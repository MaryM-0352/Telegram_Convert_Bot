package src.main.java;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.io.File;
import java.util.List;

public class Bot extends TelegramLongPollingBot {
    

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
            message = update.getMessage();
            System.out.println(message);
            if (message != null && message.hasText()) {
                String msg = message.getText().toLowerCase();

                if (msg.startsWith("/hello")) {
                    SendMsg(message, "Hello World!");
                } else if (msg.equals("/start")) {
                    SendMsg(message, "Ok! let's get the party started 😀\nWhat do you want to convert to PDF?");
                } else if (msg.startsWith("txt")) {
                    SendMsg(message, "attach file with txt");
                }
            } else if (message.hasDocument()) {
                try {
                    var doc = update.getMessage().getDocument();
                    DownFileDocx.DownFileDocx(doc);
                    System.out.println("File is downloaded");
                    String chatId = message.getChatId().toString();
                    GetFilePDF.GetFilePDF(doc);
                    System.out.println("File is converted");
                    SendDocFile(chatId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }
            } else if (message.hasPhoto()) {
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

    private void DownFilePhoto(List<PhotoSize> doc) throws IOException {
        if (doc != null){
            final String fileId = doc.get(1).getFileId();
            final String fileName = doc.get(1).getFileUniqueId();
            UploadFile.uploadFile(fileName, fileId);
        }
    }


    public void SendDocFile(String chatId) throws IOException, TelegramApiException {
        SendDocument sendDocumentRequest = new SendDocument();
        File name = new File("BlankPdf.pdf");
        sendDocumentRequest.setChatId(chatId);
        sendDocumentRequest.setDocument(new InputFile(name));
        sendDocumentRequest.setCaption("Document");
        execute(sendDocumentRequest);
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
