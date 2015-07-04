package com.remote.command.websocket.server;

/**
 * Created by saurabhchaubey on 15/06/15.
 */

import com.remote.command.websocket.data.Message;
import com.remote.command.websocket.data.MessageDecoder;
import com.remote.command.websocket.data.MessageEncoder;
import com.remote.command.websocket.data.MessageType;
import com.remote.command.websocket.domain.User;
import com.remote.command.websocket.domain.UserType;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.remote.command.websocket.server.MessageProcessorUtil.*;

@ServerEndpoint(value="/command", encoders = {MessageEncoder.class}, decoders = {MessageDecoder.class})
public class CommandServer {

    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
    private static final ConcurrentHashMap<String, User> masterUsers = new ConcurrentHashMap<String, User>();
    private static final ConcurrentHashMap<String, List<User>> slaveUsers = new ConcurrentHashMap<String, List<User>>();
    private static final ConcurrentHashMap<String, List<Session>> sessionsUserMap = new ConcurrentHashMap<String, List<Session>>();

    private Map<UserType, MessageProcessor> messageProcessorFactoryMap = new HashMap<>();
    private ServerStateHolder serverStateHolder;

    public CommandServer() {
        SlaveMessageProcessor slaveMessageProcessor = new SlaveMessageProcessor();
        MasterMessageProcessor masterMessageProcessor = new MasterMessageProcessor();
        AdminMessageProcessor adminMessageProcessor = new AdminMessageProcessor();
        serverStateHolder = new ServerStateHolder();
        slaveMessageProcessor.setServerStateHolder(serverStateHolder);
        masterMessageProcessor.setServerStateHolder(serverStateHolder);
        adminMessageProcessor.setServerStateHolder(serverStateHolder);
        messageProcessorFactoryMap.put(UserType.SLAVE, slaveMessageProcessor);
        messageProcessorFactoryMap.put(UserType.MASTER, masterMessageProcessor);
        messageProcessorFactoryMap.put(UserType.ADMIN, adminMessageProcessor);
    }

    /**
     * @OnOpen allows us to intercept the creation of a new session.
     * The session class allows us to send data to the user.
     * In the method onOpen, we'll let the user know that the handshake was
     * successful.
     */
    @OnOpen
    public void onOpen(Session session){
        serverStateHolder.addInitSession(session);
    }

    /**
     * When a user sends a message to the server, this method will intercept the message
     * and allow us to react to it. For now the message is read as a String.
     */
    @OnMessage
    public void onMessage(Message message, Session session){
        System.out.println("Message from " + session.getId() + ": " + message);
        UserType userType = UserType.valueOf(getPropertyStringValue(message, "usertype").toUpperCase());
        if(userType != null){
                handleMessage(message, session, userType);
            }else{
                sendMessage(prepareMessage(MessageType.ERROR, "UserType not defined"), session);
            }
    }

    private void handleMessage(Message message, Session session, UserType userType){
        String email = getPropertyStringValue(message, "email");
        String messageType = getPropertyStringValue(message, "messagetype");
        if(email != null){
            if(messageType != null && MessageType.REGISTER.toString().equals(messageType)){
                messageProcessorFactoryMap.get(userType).registerUser(email, message, session, userType);
            }else if(MessageType.COMMAND.toString().equals(messageType)){
                messageProcessorFactoryMap.get(userType).executeCommand(getPropertyStringValue(message,"commandstring"), email);
            }
        }
    }

   /**
     * The user closes the connection.
     *
     * Note: you can't send messages to the client from this method
     */
    @OnClose
    public void onClose(Session session){
        serverStateHolder.removeSession(session);
    }

}
