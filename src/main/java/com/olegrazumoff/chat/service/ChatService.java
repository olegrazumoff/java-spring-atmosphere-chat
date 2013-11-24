package com.olegrazumoff.chat.service;

import com.olegrazumoff.chat.model.Message;
import com.olegrazumoff.chat.model.User;
import org.atmosphere.cpr.AtmosphereResource;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Oleg
 * Date: 21.11.13
 * Time: 15:27
 * To change this template use File | Settings | File Templates.
 */
public interface ChatService {
    List<Message> getMessages();
    Collection<User> getOnlineUsers();
    void onConnect(final AtmosphereResource resource);
    void onMessage(final AtmosphereResource resource);
    void onDisconnect(final AtmosphereResource resource);
}
