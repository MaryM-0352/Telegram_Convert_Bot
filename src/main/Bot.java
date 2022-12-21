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


public class Bot extends TelegramLongPollingBot {
    final static public String BOT_TOKEN = Info.getToken();
    final private String BOT_NAME = Info.getName();

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
    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    List<String[]> group_info = new ArrayList<>();
    int current_id = -10;
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
                } else if (msg.equals("/only_one")){
                    current_id = message.getMessageId();
                } else if (msg.equals("/finish")){
                    try {
                        String type = message.getDocument().getMimeType();
                        FilePDF.getTextPDF("empty", "group", group_info, type);
                        String chatId = message.getChatId().toString();
                        SendDocFile(chatId, "text.txt");
                        group_info.clear();
                    } catch (IOException | TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else {
                assert message != null;
                if (message.hasDocument() && message.getDocument() != null) {
                    try {
                        if (!group_info.isEmpty()){
                            SendMsg(message, "Please send '/finish' when you're finished sending a group of files." +
                                                "If you want to add to group one file, send '/only_one'");
                        }
                        var groupId = update.getMessage().getMediaGroupId();
                        final String file_name = message.getDocument().getFileName();
                        final String file_id = message.getDocument().getFileId();
                        int id = message.getMessageId();
                        System.out.println(current_id);
                        if (Objects.equals(message.getDocument().getMimeType(), "text/html")){
                            System.out.println("how it can be");
                            HTML.convertHTMLToPDF(file_name);
                            System.out.println("save?");
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
                                String chatId = message.getChatId().toString();
                                FilePDF.getTextPDF(file_name, null, group_info, doc.getMimeType());
                                SendDocFile(chatId, file_name);
                            }
                        }
                    } catch (IOException | TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                } else if (message.hasPhoto()) {
                    try {
                        String chatId = update.getMessage().getChatId().toString();
                        var doc = update.getMessage().getPhoto();
                        var photoSize = Photo.getPhoto(update);
                        UploadFile.UploadPhoto("photo", getFilePath(photoSize));
                        FilePDF.getPhotoPDF("photo.jpg");
                        SendPhotoFile(chatId,"photo.jpg");
                    } catch (IOException | TelegramApiException e) {
                        throw new RuntimeException(e);
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
