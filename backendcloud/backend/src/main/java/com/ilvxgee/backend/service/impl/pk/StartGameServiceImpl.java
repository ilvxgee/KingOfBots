package com.ilvxgee.backend.service.impl.pk;

import com.ilvxgee.backend.consumer.WebSocketServer;
import com.ilvxgee.backend.service.pk.StartGameService;
import org.springframework.stereotype.Service;

@Service
public class StartGameServiceImpl implements StartGameService {
    @Override
    public String startGame(Integer aId,Integer aBotID,Integer bId,Integer bBotId) {
        System.out.println("start game:" + aId + " " + bId);
        WebSocketServer.startGame(aId,aBotID, bId,bBotId);
        return "start game success";
    }
}
