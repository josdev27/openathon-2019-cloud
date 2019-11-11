package com.openathon.demo.service;

import org.springframework.stereotype.Service;

import com.openathon.demo.service.impl.OpenathonService;

@Service
public class OpenathonServiceImpl implements OpenathonService {

    public String hello(String name) {
        return "Hello, " + name;
    }
    
}
