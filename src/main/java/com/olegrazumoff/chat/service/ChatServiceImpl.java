package com.olegrazumoff.chat.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.olegrazumoff.chat.dao.MessageDao;
import com.olegrazumoff.chat.dao.UserDao;
import com.olegrazumoff.chat.model.Message;
import com.olegrazumoff.chat.model.User;
import com.olegrazumoff.chat.model.WebSocketMessage;
import com.olegrazumoff.chat.support.AtmosphereUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.atmosphere.cpr.AtmosphereResource;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringWriter;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Oleg
 * Date: 17.11.13
 * Time: 1:09
 * To change this template use File | Settings | File Templates.
 */
@Service
public class ChatServiceImpl implements ChatService{

    public static final String DATE_FORMAT = "HH:mm:ss";
    public static final int DEFAULT_MESSAGES_TO_SHOW = 10;

    public static final Logger LOG = LoggerFactory.getLogger(ChatServiceImpl.class);

    private int messagesToShow;

    @Value("#{chatProps.messagestoshow}")
    private void setMessagesToShow(int messagesToShow) {
        if(messagesToShow < 0) {
            this.messagesToShow = DEFAULT_MESSAGES_TO_SHOW;
        } else {
            this.messagesToShow = messagesToShow;
        }
    }

    @Autowired
    private ApplicationContext ctx;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private UserDao userDao;

    public Collection<User> getOnlineUsers() {
        try {
            return userDao.getAll();
        } catch (HibernateException e) {
            LOG.warn("Something going wrong", e);
        }
        return Collections.EMPTY_SET;
    }

    private User addUser(String name, String uuid) {
        try {
            String trimmedName = name.trim();
            if(trimmedName.length() == 0) {
                return null;
            }
            String escapedName = StringEscapeUtils.escapeXml(trimmedName);
            User user = new User(escapedName, uuid);
            userDao.add(user);
            return user;
        } catch (ConstraintViolationException e) {
            LOG.info("User already exist", e);
        } catch (HibernateException e) {
            LOG.warn("Something going wrong", e);
        }
        return null;
    }

    private Message addMessage(String author, String text) {
        try {
            String trimmedText = text.trim();
            if(trimmedText.length() == 0) {
                return null;
            }
            String escapedText = StringEscapeUtils.escapeXml(trimmedText);
            Message message = new Message(author, escapedText, new Date());
            messageDao.add(message);
            return message;
        } catch (HibernateException e) {
            LOG.warn("Something going wrong", e);
        }
        return null;
    }

    public List<Message> getMessages() {
        List<Message> result = Collections.EMPTY_LIST;
        try {
            result = messageDao.get(messagesToShow);
            Collections.reverse(result);
        } catch (HibernateException e) {
            LOG.warn("Something going wrong", e);
        }
        return result;
    }

    private void sendRegister(final AtmosphereResource resource, String username) {
        Gson gson = new Gson();
        // if username does not exist in database
        User user = addUser(username, resource.uuid());
        if(user != null){
            // send to client that OK
            WebSocketMessage registerMessage = new WebSocketMessage(WebSocketMessage.REGISTER, user.getName());
            resource.getResponse().write(gson.toJson(registerMessage));
            // send other clients that new user joined
            WebSocketMessage broadcastMessage = new WebSocketMessage(WebSocketMessage.ADDUSER, user.getName());
            AtmosphereUtils.broadcast(gson.toJson(broadcastMessage));
        }  else {
            WebSocketMessage registerMessage = new WebSocketMessage(WebSocketMessage.REGISTER, "FAIL");
            resource.getResponse().write(gson.toJson(registerMessage));
        }
    }

    private void sendMessage(final AtmosphereResource resource, String message) {
        Gson gson = new GsonBuilder().setDateFormat("HH:mm:ss").create();
        User user = userDao.get(resource.uuid());
        if(user != null) {
            Message newMessage = addMessage(user.getName(), message);
            if(newMessage != null) {
                WebSocketMessage wsMessage = new WebSocketMessage(WebSocketMessage.MESSAGE, gson.toJson(newMessage));
                AtmosphereUtils.broadcast(gson.toJson(wsMessage));
            } else {
                WebSocketMessage wsMessage = new WebSocketMessage(WebSocketMessage.ERROR, "Can't send message");
                resource.getResponse().write(gson.toJson(wsMessage));
            }
        } else {
            WebSocketMessage wsMessage = new WebSocketMessage(WebSocketMessage.ERROR, "Something going wrong");
            resource.getResponse().write(gson.toJson(wsMessage));
        }
    }

    public void onConnect(final AtmosphereResource resource) {
        AtmosphereUtils.suspend(resource, ctx);
    }

    public void onMessage(final AtmosphereResource resource)  {
        Gson gson = new Gson();
        String message;
        try {
            message = resource.getRequest().getReader().readLine();
        } catch (IOException e) {
            LOG.error("Can't read request message", e);
            WebSocketMessage wsMessage = new WebSocketMessage(WebSocketMessage.ERROR, "Something going wrong");
            resource.getResponse().write(gson.toJson(wsMessage));
            return;
        }

        WebSocketMessage msg = gson.fromJson(message, WebSocketMessage.class);
        // recieve new user registration
        if(WebSocketMessage.REGISTER.equals(msg.getType())) {
            sendRegister(resource, msg.getData());
        // recieve new message
        } else if(WebSocketMessage.MESSAGE.equals(msg.getType())) {
            sendMessage(resource, msg.getData());
        }
    }

    public void onDisconnect(final AtmosphereResource resource) {
        try {
            User user = userDao.get(resource.uuid());
            if(user != null) {
                userDao.remove(user);
                WebSocketMessage message = new WebSocketMessage(WebSocketMessage.REMOVEUSER, user.getName());
                Gson gson = new Gson();
                AtmosphereUtils.broadcast(gson.toJson(message));
            }
        } catch (HibernateException e) {
            LOG.warn("Something going wrong", e);
        }
    }
}
