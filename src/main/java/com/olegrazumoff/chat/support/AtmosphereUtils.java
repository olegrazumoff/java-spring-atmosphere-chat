/*
 * Copyright 2002-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.olegrazumoff.chat.support;

import com.olegrazumoff.chat.service.ChatServiceImpl;
import org.atmosphere.cpr.*;
import org.atmosphere.websocket.WebSocketEventListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import javax.servlet.http.HttpServletRequest;

public final class AtmosphereUtils {

    public static final Logger LOG = LoggerFactory.getLogger(AtmosphereUtils.class);

    private static final Broadcaster broadcaster = BroadcasterFactory.getDefault().lookup("myuuid", true);

    public static AtmosphereResource getAtmosphereResource(HttpServletRequest request) {
        return getMeteor(request).getAtmosphereResource();
    }
    public static Meteor getMeteor(HttpServletRequest request) {
        return Meteor.build(request);
    }
    public static void suspend(final AtmosphereResource resource, final ApplicationContext ctx) {

        resource.addEventListener(new WebSocketEventListenerAdapter() {

            @Override
            public void onDisconnect(WebSocketEvent event) {
                ChatServiceImpl chat = ctx.getBean(ChatServiceImpl.class);
                chat.onDisconnect(event.webSocket().resource());
            }
        });

        broadcaster.addAtmosphereResource(resource);

        if (AtmosphereResource.TRANSPORT.LONG_POLLING.equals(resource.transport())) {
            resource.resumeOnBroadcast(true).suspend();
        } else {
            resource.suspend();
        }
    }

    public static void broadcast(String data) {
        broadcaster.broadcast(data);
    }

}