package com.remote.command.websocket.server;

import com.remote.command.websocket.data.Message;
import com.remote.command.websocket.data.MessageType;
import com.remote.command.websocket.data.ResponseStatus;
import com.remote.command.websocket.domain.User;
import com.remote.command.websocket.domain.UserType;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.remote.command.websocket.server.MessageProcessorUtil.*;

/**
 * Created by saurabhchaubey on 28/06/15.
 */
public class ServerStateHolder {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<Session>());
    private static final ConcurrentHashMap<User, Session> userSessionMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Session, User> sessionUserMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, User> masterUsers = new ConcurrentHashMap<String, User>();
    private static final ConcurrentHashMap<String, List<User>> slaveUsers = new ConcurrentHashMap<String, List<User>>();

    public void addInitSession(Session session){
        System.out.println(session.getId() + " has opened a connection");
        session.setMaxIdleTimeout(3600000);
        sessions.add(session);
        sendMessage(prepareMessage(MessageType.INFO, " Connected session sessionId: " + session.getId()), session);
    }

    public synchronized void registerSlave(String email, Message message, Session session, UserType userType){
        String password = getPropertyStringValue(message, "password");
        slaveUsers.putIfAbsent(email, new ArrayList<User>());
        List<User> tmpList = new ArrayList<User>();
        if(masterUsers.get(email) != null){
            tmpList.add(masterUsers.get(email));
        }
        tmpList.addAll(slaveUsers.get(email));
        if(password != null && passwordMatchesExistingUsers(password, tmpList)){
            User user = createUser(email, password,
                    userType, session.getId());
            slaveUsers.get(email).add(user);
            sessionUserMap.put(session, user);
            userSessionMap.put(user, session);
            sendMessage(prepareMessage(ResponseStatus.SUCCESS, "Welcome Slave "+email), session);
        }else{
            sendMessage(prepareMessage(ResponseStatus.FAILURE, "There password provided is either incorrect or empty for : "+email), session);
        }
    }

    public synchronized void registerMaster(String email, Message message, Session session, UserType userType){
        String password = getPropertyStringValue(message, "password");
        if(masterUsers.containsKey(email)){
            sendMessage(prepareMessage(ResponseStatus.FAILURE, "There is already a MASTER user with the email : "+email), session);
        }else{
            if(password != null && passwordMatchesExistingUsers(password, slaveUsers.get(email))){
                User user = createUser(email, password,
                        userType, session.getId());
                masterUsers.put(email, user);
                sessionUserMap.put(session, user);
                userSessionMap.put(user, session);
                sendMessage(prepareMessage(ResponseStatus.SUCCESS, "Welcome Master "+email), session);
            }else{
                sendMessage(prepareMessage(ResponseStatus.FAILURE, "There password provided is either incorrect or empty for : "+email), session);
            }
        }
    }

    public synchronized void registerAdmin(String email, Message message, Session session, UserType userType){
        String password = getPropertyStringValue(message, "password");
        if(email != null && password != null
                && email.equals("email@gmail.com")
                && password.equals("adminpassword")){
                sendMessage(prepareMessage(ResponseStatus.SUCCESS, "Welcome Admin....Just Rock the users"), session);
        }else{
            sendMessage(prepareMessage(ResponseStatus.FAILURE, "Invalid Admin User"), session);
        }
    }


    private boolean passwordMatchesExistingUsers(String password, List<User> users) {
        for(User user :users){
            if(!user.getPassword().equals(password)){
                return false;
            }
        }
        return true;
    }

    public void removeSession(Session session){
        sessions.remove(session);
        sessionUserMap.remove(session);
        User user = sessionUserMap.get(session);
        if(user != null){
            if(user.getType() == UserType.MASTER){
                masterUsers.remove(user.getEmail());
            }else if(user.getType() == UserType.SLAVE){
                List<User> slaves = slaveUsers.get(user.getEmail());
                if(slaves != null && !slaves.isEmpty()){
                    Iterator<User> userIterator = slaves.iterator();
                    for(;userIterator.hasNext();){
                        User usr = userIterator.next();
                        if(user.equals(usr)){
                            userIterator.remove();
                            break;
                        }
                    }
                }

            }else if(user.getType() == UserType.ADMIN){

            }
            userSessionMap.remove(user);
        }
        System.out.println("Session " +session.getId()+" has ended");
        sendMessage(prepareMessage(MessageType.INFO, " Disconnected session sessionId: " + session.getId()), session);
    }

    public void sendMessageToAll(Message message){
        for(Session s : sessions){
            try {
                s.getBasicRemote().sendObject(message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }catch(EncodeException ex){
                ex.printStackTrace();
            }
        }
    }

    public void sendMessageToSessionsOfSpecificUser(String email, Message message){
        List<User> slaves = slaveUsers.get(email);
        for(User slave : slaves){
            Session session = userSessionMap.get(slave);
            if(session != null){
                sendMessage(message, session);
            }
        }
    }
}
