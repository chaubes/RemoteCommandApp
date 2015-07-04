package com.remote.command.websocket.server;

import com.remote.command.websocket.data.Message;
import com.remote.command.websocket.domain.UserType;

import javax.websocket.Session;

/**
 * Created by saurabhchaubey on 28/06/15.
 */
public interface MessageProcessor {

    void registerUser(String email, Message message, Session session, UserType userType);

    void setServerStateHolder(ServerStateHolder serverStateHolder);

    void executeCommand(String commandstring, String email);
}
