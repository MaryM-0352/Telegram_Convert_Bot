package src.main;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Bot extends TelegramLongPollingBot {
    final protected static String BOT_TOKEN = Info.getToken();

    final protected String BOT_NAME = Info.getName();

    public String getFilePath(PhotoSize photo){
        Objects.requireNonNull(photo);
        GetFile getFileMethod = new GetFile();
        getFileMethod.setFileId(photo.getFileId());
        try {
            org.telegram.telegrambots.meta.api.objects.File file = execute(getFileMethod);
            return file.getFilePath();
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> collectPhoto(List<PhotoSize> list) throws IOException {
        List<String> names = new ArrayList<>();
        int i = 0;
        for (PhotoSize photo: list){
            i++;
            UploadFile.UploadPhoto("photo" + i, getFilePath(photo));
            names.add("photo" + i + ".jpg");
        }
        return names;
    }
    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    List<String[]> group_info = new ArrayList<>();
    List<PhotoSize> photoInfo = new ArrayList<>();
    int current_id = -10;
    String type;
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
                System.out.println("It is a text");
                if (message.getText().startsWith("https://")){
                    System.out.println("start");
                    String url = message.getText();
                    HTML.convertURLToPDF(url);
                    File file = new File("WebToPdf" + ".pdf");
                    while (!file.exists()){
                        try {
                            TimeUnit.SECONDS.sleep(1);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    String chatId = message.getChatId().toString();
                    try {
                        SendDocFile(chatId, "WebToPdf");
                    } catch (IOException | TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }

                String msg = message.getText().toLowerCase();

                if (msg.startsWith("/hello")) {
                    SendMsg(message, "Hello World!");
                } else if (msg.equals("/start")) {
                    SendMsg(message, "Ok! let's get the party started 😀\nWhat do you want to convert to PDF?");
                } else if (msg.equals("/only_one")){
                    current_id = message.getMessageId();
                } else if (msg.equals("/finish")){
                    String chatId = message.getChatId().toString();
                    if (!group_info.isEmpty()) {
                        try {
                            FilePDF.getTextPDF("empty", "group", group_info, type);
                            SendDocFile(chatId, "text.txt");
                            group_info.clear();
                        } catch (IOException | TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    } else if (!photoInfo.isEmpty()){
                        try {
                            FilePDF.getPhotoPDF(collectPhoto(photoInfo), null, "group");
                            SendPhotoFile(chatId, "photos");
                            photoInfo.clear();
                        } catch (IOException | TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            } else {
                assert message != null;
                if (message.hasDocument() && message.getDocument() != null) {
                    try {
                        type = message.getDocument().getMimeType();
                        if (!group_info.isEmpty()){
                            SendMsg(message, "Please send '/finish' when you're finished sending a group of files." +
                                                "If you want to add to group one file, send '/only_one'");
                        }
                        var groupId = update.getMessage().getMediaGroupId();
                        final String file_name = message.getDocument().getFileName();
                        final String file_id = message.getDocument().getFileId();
                        int id = message.getMessageId();
                        String chatId = message.getChatId().toString();
                        System.out.println(current_id);
                        if (Objects.equals(message.getDocument().getMimeType(), "text/html")){
                            UploadFile.uploadFile(file_name, file_id);
                            String PDFName = file_name.split("\\.")[0];
                            HTML.convertHTMLToPDF(file_name);
                            File file = new File(PDFName + ".pdf");
                            while (!file.exists()){
                                TimeUnit.SECONDS.sleep(1);
                            }
                            SendDocFile(chatId, PDFName);
                        } else {
                            if (id == current_id + 1) {
                                groupId = "group";
                            }
                            if (groupId != null) {
                                String[] info = new String[2];
                                info[0] = file_name;
                                info[1] = file_id;
                                group_info.add(info);
                            } else {
                                var doc = update.getMessage().getDocument();
                                UploadFile.uploadFile(file_name, file_id);
                                FilePDF.getTextPDF(file_name, null, group_info, doc.getMimeType());
                                SendDocFile(chatId, file_name);
                            }
                        }
                    } catch (IOException | TelegramApiException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else if (message.hasPhoto()) {
                    int id = message.getMessageId();
                    var groupId = update.getMessage().getMediaGroupId();
                    if (!photoInfo.isEmpty()){
                        SendMsg(message, "Please send '/finish' when you're finished sending a group of files.");
                    }
                    System.out.println(groupId);
                    if (id == current_id + 1) {
                        groupId = "group";
                    }
                    if (groupId != null) {
                        var photoSize = Photo.getPhoto(update);
                        photoInfo.add(photoSize);
                        System.out.println(photoInfo.size());
                    } else {
                        try {
                            String chatId = update.getMessage().getChatId().toString();
                            var photoSize = Photo.getPhoto(update);
                            UploadFile.UploadPhoto("photo", getFilePath(photoSize));
                            FilePDF.getPhotoPDF(null, "photo.jpg", null);
                            SendPhotoFile(chatId, "photo.jpg");
                        } catch (IOException | TelegramApiException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }

    public void SendDocFile(String chatId, String filename) throws IOException, TelegramApiException {
        System.out.print("It is SendFunction");
        SendDocument sendDocumentRequest = new SendDocument();
        File name = new File(filename + ".pdf");
        sendDocumentRequest.setChatId(chatId);
        sendDocumentRequest.setDocument(new InputFile(name));
        sendDocumentRequest.setCaption("Document");
        execute(sendDocumentRequest);
    }

    public void SendPhotoFile(String chatId, String filename) throws IOException, TelegramApiException {
        System.out.print("It is SendFunction");
        SendDocument sendDocumentRequest = new SendDocument();
        File name = new File(filename + ".pdf");
        sendDocumentRequest.setChatId(chatId);
        sendDocumentRequest.setDocument(new InputFile(name));
        sendDocumentRequest.setCaption("Photo");
        execute(sendDocumentRequest);
    }

        private void SendMsg (Message message, String s){
            SendMessage sendMessage = new SendMessage();

            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText(s);
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
