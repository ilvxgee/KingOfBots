package com.ilvxgee.backend.controller.user.bot;

import com.ilvxgee.backend.service.user.bot.UpdateBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UpdateBotController {
    @Autowired
    private UpdateBotService updateBotService;

    @PostMapping("/api/user/bot/update/")
    public Map<String, String> UpdateBot(@RequestParam Map<String, String> data) {
        return updateBotService.update(data);
    }

}
