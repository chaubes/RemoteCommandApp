package com.remote.command.websocket.server;

import com.remote.command.websocket.data.Message;
import com.remote.command.websocket.data.MessageType;
import com.remote.command.websocket.data.ResponseStatus;
import com.remote.command.websocket.domain.User;
import com.remote.command.websocket.domain.UserType;

import javax.json.Json;
import javax.json.JsonObject;
import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;

/**
 * Created by saurabhchaubey on 28/06/15.
 */
public class MessageProcessorUtil {

    public static String getPropertyStringValue(Message message, String property){
        JsonObject jsonObject = message.getJson();
        if(jsonObject != null && jsonObject.getString(property) != null){
            if(property.equals("password")){
                return jsonObject.getString(property);
            }else if(property.equals("messagetype")){
                return jsonObject.getString(property).trim().toUpperCase();
            }
            return jsonObject.getString(property).trim().toLowerCase();
        }
        return null;
    }

    public static void sendMessage(Message message, Session session){
        try {
            session.getBasicRemote().sendObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncodeException e) {
            e.printStackTrace();
        }
    }


    public static Message prepareMessage(ResponseStatus status, String displaytext){
        Message message = new Message(Json.createObjectBuilder()
                .add("status", status.toString())
                .add("message", displaytext)
                .build());
        return message;
    }

    public static Message prepareMessage(MessageType type, String displaytext){
        Message message = new Message(Json.createObjectBuilder()
                .add("type", type.toString())
                .add("message", displaytext)
                .build());
        return message;
    }

    public static Message prepareMessage(MessageType type, String displaytext, String data){
        Message message = new Message(Json.createObjectBuilder()
                .add("type", type.toString())
                .add("message", displaytext)
                .add("data", data)
                .build());
        return message;
    }

    public static User createUser(String email, String password, UserType userType, String id) {
        return new User(email, password, userType, id);
    }
}
