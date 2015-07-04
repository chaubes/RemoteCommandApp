package com.remote.command.websocket.server;

import com.remote.command.websocket.data.Message;
import com.remote.command.websocket.domain.UserType;

import javax.websocket.Session;

/**
 * Created by saurabhchaubey on 28/06/15.
 */
public class SlaveMessageProcessor implements MessageProcessor {
    private ServerStateHolder serverStateHolder;

    @Override
    public void registerUser(String email, Message message, Session session, UserType userType) {
        serverStateHolder.registerSlave(email, message, session, userType);
    }

    @Override
    public void setServerStateHolder(ServerStateHolder serverStateHolder) {
        this.serverStateHolder = serverStateHolder;
    }

    @Override
    //NoOp
    public void executeCommand(String commandstring, String email) {

    }
}
