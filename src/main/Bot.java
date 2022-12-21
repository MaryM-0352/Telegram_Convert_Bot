package src.main;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.io.File;
import java.util.*;


//ДЕЛА НАСУЩНЫЕ: скачать картинку в норм форме(чтобы читалось)
public class Bot extends TelegramLongPollingBot {
    final static public String BOT_TOKEN = Info.getToken();
    final private String BOT_NAME = Info.getName();

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
                        GetFilePDF.GetFilePDF("empty", "group", group_info);
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
                                DownFileDocx.DownFileDocx(file_name, file_id);
                                String chatId = message.getChatId().toString();
                                GetFilePDF.GetFilePDF(file_name, null, group_info);
                                SendDocFile(chatId, file_name);
                            }
                        }
                    } catch (IOException | TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                } else if (message.hasPhoto()) {
                    try {
                        String chat_id = update.getMessage().getChatId().toString();
                        var doc = update.getMessage().getPhoto();
                        DownFilePhoto.DownFilePhoto(doc);
                        final String fileId = doc.get(0).getFileId();
                        GetPhotoPDF.GetPhotoPDF(fileId);
                        SendDocFile(chat_id, "empty");
                        //GetFilePDF.GetFilePDF(doc);
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
