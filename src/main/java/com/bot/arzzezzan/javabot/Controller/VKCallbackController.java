//package com.bot.arzzezzan.javabot.Controller;
//
//import com.bot.arzzezzan.javabot.Command.AuthCommand;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@Controller
//@RequestMapping("/https://oauth.vk.com/blank.html")  // Укажите путь, который будет обрабатывать колбэк VK
//public class VKCallbackController {
//
//    @Autowired
//    private AuthCommand authCommand;  // Инъектируйте экземпляр AuthCommand
//
//    @GetMapping
//    public String handleVKCallback(@RequestParam("code") String authorizationCode) {
//        authCommand.handleAuthorizationResponse(authorizationCode);
//        return "callback-success";
//    }
//}
