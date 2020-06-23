package com.walter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
public class DemoController {
    @Autowired
    private DataSource dataSource;

    @GetMapping("/ping")
    public String ping(){
        return "success";
    }
}
