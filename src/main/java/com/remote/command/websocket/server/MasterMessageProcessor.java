package com.remote.command.websocket.server;

import com.remote.command.websocket.data.Message;
import com.remote.command.websocket.data.MessageType;
import com.remote.command.websocket.domain.UserType;

import javax.websocket.Session;

import static com.remote.command.websocket.server.MessageProcessorUtil.prepareMessage;

/**
 * Created by saurabhchaubey on 28/06/15.
 */
public class MasterMessageProcessor implements MessageProcessor {
    private ServerStateHolder serverStateHolder;

    @Override
    public void registerUser(String email, Message message, Session session, UserType userType) {
        serverStateHolder.registerMaster(email, message, session, userType);
    }

    @Override
    public void setServerStateHolder(ServerStateHolder serverStateHolder) {
        this.serverStateHolder = serverStateHolder;
    }

    @Override
    public void executeCommand(String commandstring, String email) {
        Message messageToSend = prepareMessage(MessageType.COMMAND, "About to execute command....",
                commandstring);
        serverStateHolder.sendMessageToSessionsOfSpecificUser(email, messageToSend);
    }

}
