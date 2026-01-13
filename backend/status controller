package com.ripae_co.REST_APIs.controller;

import com.ripae_co.REST_APIs.entity.Status;
import com.ripae_co.REST_APIs.repository.StatusRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/status")

public class StatusController
{
    private final StatusRepository statusRepository;

    public StatusController (StatusRepository statusRepository)
    {
        this.statusRepository=statusRepository;
    }
    @GetMapping
    public List<Status> getAllStatus()
    {
        return statusRepository.findAll();
    }
    @PostMapping
    public Status createStatus(@RequestBody Status status)
    {
        return statusRepository.save(status);
    }

}
