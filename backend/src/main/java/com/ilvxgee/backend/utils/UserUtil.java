package com.ilvxgee.backend.utils;

import com.ilvxgee.backend.pojo.User;
import com.ilvxgee.backend.service.impl.utils.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;


public class UserUtil {
    public static User getUser() {
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        return loginUser.getUser();
    }

}
