package com.olegrazumoff.chat.controller;

import com.olegrazumoff.chat.service.ChatServiceImpl;
import org.atmosphere.cpr.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
public class ChatController {

    @Autowired
    private ChatServiceImpl chat;

    @RequestMapping(value="/", method = RequestMethod.GET)
	public String chat(ModelMap model) {
        model.addAttribute("messages", chat.getMessages());
        model.addAttribute("users", chat.getOnlineUsers());
        model.addAttribute("dateFormat", ChatServiceImpl.DATE_FORMAT);
		return "chat";
	}

    @RequestMapping(value = "/websockets", method = RequestMethod.GET)
    @ResponseBody
    public void wsGet(final AtmosphereResource event) throws IOException {
        chat.onConnect(event);
    }

    @RequestMapping(value = "/websockets", method = RequestMethod.POST)
    @ResponseBody
    public void wsPost(final AtmosphereResource resource) throws IOException {
        chat.onMessage(resource);
    }

}