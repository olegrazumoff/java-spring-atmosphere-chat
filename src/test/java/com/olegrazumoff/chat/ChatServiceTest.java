package com.olegrazumoff.chat;

import com.olegrazumoff.chat.dao.MessageDao;
import com.olegrazumoff.chat.dao.UserDao;
import com.olegrazumoff.chat.model.Message;
import com.olegrazumoff.chat.service.ChatServiceImpl;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Oleg
 * Date: 21.11.13
 * Time: 13:10
 * To change this template use File | Settings | File Templates.
 */
public class ChatServiceTest extends AppTests {

    @Value("#{chatProps.messagestoshow}")
    private int messagesToShow;

    @Autowired
    private ChatServiceImpl chatService;

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private UserDao userDao;

    @Before
    public void setup() {
        messageDao.truncate();
        userDao.truncate();
    }

    @Test
    public void getMessagesTest() throws InterruptedException {
        List<Message> messages = chatService.getMessages();
        Assert.assertEquals("Wrong number of messages", 0, messages.size());
        Message message = new Message("author", "text", new Date());
        messageDao.add(message);
        messages = chatService.getMessages();
        Assert.assertEquals("Wrong number of messages", 1, messages.size());
        Thread.sleep(1000);
        Message message2 = new Message("author", "text", new Date());
        messageDao.add(message2);
        messages = chatService.getMessages();
        Assert.assertEquals("Wrong number of messages", 2, messages.size());
        Assert.assertTrue("Messages in wrong order", messages.get(0).getDate().getTime() < messages.get(1).getDate().getTime());
        for(int i = 0; i < messagesToShow; i++) {
            messageDao.add(new Message("author", "text", new Date()));
        }
        Assert.assertEquals("Wrong number of messages", messagesToShow, chatService.getMessages().size());
    }

}
