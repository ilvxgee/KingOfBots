package com.ilvxgee.backend.controller.user.bot;

import com.ilvxgee.backend.service.user.bot.AddBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class AddBotController {
    @Autowired
    private AddBotService addBotService;

    @PostMapping("/user/bot/add/")
    public Map<String,String> add(@RequestParam Map<String,String> data){


        return addBotService.add(data);
    }
}
