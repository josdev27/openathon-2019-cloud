package com.openathon.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openathon.demo.service.impl.OpenathonService;


@RestController
@RequestMapping(value = "openathon/", produces = MediaType.APPLICATION_JSON_VALUE)
public class OpenathonController {

    @Autowired
    private OpenathonService openathonService;
    
    @GetMapping(value = "/hello")
    public ResponseEntity<String> getHello(@RequestParam("name") String name) {
        return ResponseEntity.ok().body(openathonService.hello(name));
    }
}
