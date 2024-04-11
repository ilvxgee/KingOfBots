package com.ilvxgee.backend.consumer;

import com.alibaba.fastjson2.JSONObject;
import com.ilvxgee.backend.consumer.utils.Game;
import com.ilvxgee.backend.consumer.utils.JwtAuthentication;
import com.ilvxgee.backend.mapper.RecordMapper;
import com.ilvxgee.backend.mapper.UserMapper;
import com.ilvxgee.backend.pojo.User;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Component
@ServerEndpoint(value = "/websocket/{token}")  // 注意不要以'/'结尾
public class WebSocketServer {
    public static final ConcurrentHashMap<Integer, WebSocketServer> users = new ConcurrentHashMap<>();
    private static final CopyOnWriteArraySet<User> matchPool = new CopyOnWriteArraySet<>();
    private User user;
    private Session session = null;
    private static UserMapper userMapper;
    public static RecordMapper recordMapper;
    private Game game = null;
    private static RestTemplate restTemplate;
    private final static String addPlayerUrl="http://127.0.0.1:3001/player/add/";
    private final static String removePlayerUrl="http://127.0.0.1:3001/player/remove/";


    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        WebSocketServer.userMapper = userMapper;
    }

    @Autowired
    public void setRecordMapper(RecordMapper recordMapper) {
        WebSocketServer.recordMapper = recordMapper;
    }

    @Autowired
    public void setRestTemplate(RestTemplate restTemplate){
        WebSocketServer.restTemplate=restTemplate;
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
        System.out.println("disconnected!");
//
//        if (this.user != null) {
//            users.remove(this.user.getId());
//            matchPool.remove(this.user);
//        }
    }

    private void startMatching() {
        System.out.println("start matching!");

//        matchPool.add(this.user);
//        while (matchPool.size() >= 2) {
//            Iterator<User> it = matchPool.iterator();
//            User a = it.next(), b = it.next();
//            matchPool.remove(a);
//            matchPool.remove(b);
//
//        }

        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        data.add("rating", this.user.getRating().toString());
        restTemplate.postForObject(addPlayerUrl,data,String.class);


    }


    public static void startGame(Integer aId, Integer bId) {
        User a = userMapper.selectById(aId), b = userMapper.selectById(bId);

        Game game = new Game(13, 14, 20, a.getId(), b.getId());
        game.createMap();


        if (users.get(a.getId()) != null)
            users.get(a.getId()).game = game;
        if (users.get(b.getId()) != null)
            users.get(b.getId()).game = game;


        game.start();
        JSONObject respGame = new JSONObject();
        respGame.put("a_id", game.getPlayerA().getId());
        respGame.put("a_sx", game.getPlayerA().getSx());
        respGame.put("a_sy", game.getPlayerA().getSy());
        respGame.put("b_id", game.getPlayerB().getId());
        respGame.put("b_sx", game.getPlayerB().getSx());
        respGame.put("b_sy", game.getPlayerB().getSy());
        respGame.put("map", game.getG());

        JSONObject respA = new JSONObject();
        respA.put("event", "start-matching");
        respA.put("opponent_username", b.getUsername());
        respA.put("opponent_photo", b.getPhoto());
        respA.put("game", respGame);

        if (users.get(a.getId()) != null)
            users.get(a.getId()).sendMessage(respA.toJSONString());

        JSONObject respB = new JSONObject();
        respB.put("event", "start-matching");
        respB.put("opponent_username", a.getUsername());
        respB.put("opponent_photo", a.getPhoto());
        respB.put("game", respGame);

        if (users.get(a.getId()) != null)
            users.get(b.getId()).sendMessage(respB.toJSONString());

    }

    private void stopMatching() {
        System.out.println("stop matching!");
//        matchPool.remove(this.user);

        MultiValueMap<String,String> data=new LinkedMultiValueMap<>();
        data.add("user_id", this.user.getId().toString());
        restTemplate.postForObject(removePlayerUrl,data,String.class);


    }

    private void move(int direction) {
        if (game.getPlayerA().getId().equals(user.getId())) {
            game.setNextStepA(direction);
        } else if (game.getPlayerB().getId().equals(user.getId())) {
            game.setNextStepB(direction);
        }
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
        } else if ("move".equals(event)) {
            move(data.getInteger("direction"));
            System.out.println(data.getInteger("direction"));
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

