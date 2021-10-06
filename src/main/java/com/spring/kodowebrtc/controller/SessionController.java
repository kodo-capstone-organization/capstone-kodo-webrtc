package com.spring.kodowebrtc.controller;

import com.spring.kodowebrtc.util.CryptographicHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/session")
public class SessionController
{
    @GetMapping("/getUniqueSessionLink/{sessionName}")
    public String getUniqueSessionLink(@PathVariable String sessionName)
    {
        String salt = CryptographicHelper.generateRandomString(64);
        return CryptographicHelper.getSHA256Digest(sessionName, salt);
    }
}