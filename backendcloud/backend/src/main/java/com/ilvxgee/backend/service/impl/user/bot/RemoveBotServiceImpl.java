package com.ilvxgee.backend.service.impl.user.bot;

import com.ilvxgee.backend.mapper.BotMapper;
import com.ilvxgee.backend.pojo.Bot;
import com.ilvxgee.backend.pojo.User;
import com.ilvxgee.backend.service.impl.utils.UserDetailsImpl;
import com.ilvxgee.backend.service.user.bot.RemoveBotService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
@Service
public class RemoveBotServiceImpl implements RemoveBotService {
    @Autowired
    private BotMapper botMapper;

    @Override
    public Map<String, String> remove(Map<String, String> data) {

        UsernamePasswordAuthenticationToken authenticationToken=
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser= (UserDetailsImpl) authenticationToken.getPrincipal();
        User user=loginUser.getUser();

        int bot_id=Integer.parseInt(data.get("bot_id"));

        Bot bot=botMapper.selectById(bot_id);
        Map<String,String> map=new HashMap<>();
        if(bot==null){
            map.put("error_message","Bot不存在或者已被删除");
            return map;
        }
        if(!Objects.equals(bot.getUserId(), user.getId())){
            map.put("error_message","没有权限删除该Bot");
            return map;
        }
        botMapper.deleteById(bot_id);
        map.put("error_message","success");
        return map;
    }
}