package com.ilvxgee.backend.controller.user.bot;

import com.ilvxgee.backend.pojo.Bot;
import com.ilvxgee.backend.service.user.bot.GetBotListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetBotListController {
    @Autowired
    private GetBotListService getBotListService;

    @GetMapping("/user/bot/getList/")

    public List<Bot> getBotList() {
        return getBotListService.getBotList();

    }
}
