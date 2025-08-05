package com.example.demo_task2.gmail;

import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.*;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

public class GmailService {

    private static final String APPLICATION_NAME = "Gmail API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json"; // place inside /resources

    private static Gmail getService() throws IOException, GeneralSecurityException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        InputStream in = GmailService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user"))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    // public static List<Map<String, String>> listEmails() throws Exception {
    //     Gmail service = getService();
    //     ListMessagesResponse messagesResponse = service.users().messages().list("me")
    //             .setMaxResults(200L)
    //             .execute();

    //     List<Map<String, String>> emails = new ArrayList<>();
    //     for (Message message : messagesResponse.getMessages()) {
    //         Message fullMessage = service.users().messages().get("me", message.getId()).execute();
    //         String subject = "", from = "";
    //         for (MessagePartHeader header : fullMessage.getPayload().getHeaders()) {
    //             if (header.getName().equalsIgnoreCase("Subject")) {
    //                 subject = header.getValue();
    //             } else if (header.getName().equalsIgnoreCase("From")) {
    //                 from = header.getValue();
    //             }
    //         }
    //         Map<String, String> map = new HashMap<>();
    //         map.put("from", from);
    //         map.put("subject", subject);
    //         emails.add(map);
    //     }
    //     return emails;
    // }
    public static List<Map<String, String>> listEmails() throws Exception {
    Gmail service = getService();

    // Fetch only 50 messages for now (change later to 200)
    ListMessagesResponse messagesResponse = service.users().messages().list("me")
            .setMaxResults(200L) // reduce to 10 or 50 while testing
            .execute();

    List<Message> messages = messagesResponse.getMessages();
    if (messages == null) return Collections.emptyList();

    // Use parallel stream to speed up network calls
    return messages.parallelStream().map(message -> {
        try {
            Message fullMessage = service.users().messages().get("me", message.getId())
                    .setFormat("metadata")
                    .setMetadataHeaders(Arrays.asList("Subject", "From"))
                    .execute();

            String subject = "", from = "";
            for (MessagePartHeader header : fullMessage.getPayload().getHeaders()) {
                if (header.getName().equalsIgnoreCase("Subject")) {
                    subject = header.getValue();
                } else if (header.getName().equalsIgnoreCase("From")) {
                    from = header.getValue();
                }
            }

            Map<String, String> map = new HashMap<>();
            map.put("from", from.replaceAll("(?i)\\u003c", "<").replaceAll("(?i)\\u003e", ">"));


            map.put("subject", subject);
            return map;
        } catch (IOException e) {
            e.printStackTrace(); // optional: log or ignore failures
            return null;
        }
    }).filter(Objects::nonNull).toList();
}

}
