package com.ilvxgee.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.ilvxgee.backend.consumer.utils.Game;
import com.ilvxgee.backend.consumer.utils.JwtAuthentication;
import com.ilvxgee.backend.mapper.UserMapper;
import com.ilvxgee.backend.pojo.User;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value = "/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    private static final ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    private static final CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();
    private User user;
    private Session session = null;
    private static UserMapper userMapper;



    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @OnOpen
    public void onOpen(Session session, @PathParam("token") String token) throws IOException {
        // 建立连接
        this.session = session;
        System.out.println("connected!");

        Integer userId = JwtAuthentication.getUserId(token);

        this.user = userMapper.selectById(userId);
        if (this.user != null) {
            users.put(userId, this);

        } else {
            this.session.close();
        }

        System.out.println(users);


    }

    @OnClose
    public void onClose() {
        //
        System.out.println("disconnected!");

        if (this.user != null) {
            users.remove(this.user.getId());
            matchPool.remove(this.user);
        }
    }

    private void startMatching() {

        System.out.println("start matching!");
        matchPool.add(this.user);

        while(matchPool.size()>=2){
            Iterator<User> it =matchPool.iterator();
            User a=it.next(),b=it.next();
            matchPool.remove(a);
            matchPool.remove(b);

            Game game =new Game(13,14,20);
            game.createMap();


            JSONObject respA=new JSONObject();
            respA.put("event","start-matching");
            respA.put("opponent_username",b.getUsername());
            respA.put("opponent_photo",b.getPhoto());
            respA.put("gamemap",game.getG());



            users.get(a.getId()).sendMessage(respA.toJSONString());



            JSONObject respB=new JSONObject();
            respB.put("event","start-matching");
            respB.put("opponent_username",a.getUsername());
            respB.put("opponent_photo",a.getPhoto());
            respB.put("gamemap",game.getG());
            users.get(b.getId()).sendMessage(respB.toJSONString());


        }
    }

    private void stopMatching() {
        System.out.println("stop matching!");
        matchPool.remove(this.user);

    }


    @OnMessage
    public void onMessage(String message, Session session) {//当做路由
        // 从Client接收消息
        System.out.println("receive message!");


        JSONObject data = JSONObject.parseObject(message);
        String event = data.getString("event");

        if (Objects.equals("start-matching", event)) {
            startMatching();
        } else if ("stop-matching".equals(event)) {
            stopMatching();
        }
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    public void sendMessage(String message) {
        synchronized (this.session) {
            try {
                this.session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

