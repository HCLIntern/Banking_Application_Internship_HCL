package com.ripae_co.REST_APIs.controller;

import com.ripae_co.REST_APIs.entity.App_User;
import com.ripae_co.REST_APIs.repository.App_UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/app_user")

public class AppUserController
{
    private final App_UserRepository appUserRepository;

    public AppUserController (App_UserRepository appUserRepository)
    {
        this.appUserRepository=appUserRepository;
    }
    @GetMapping
    public List<App_User> getAllApp_User()
    {
        return appUserRepository.findAll();
    }
    @PostMapping
    public App_User createApp_User(@RequestBody App_User appUser)
    {
        return appUserRepository.save(appUser);
    }

}
