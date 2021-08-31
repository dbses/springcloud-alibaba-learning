package com.github.open.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yanglulu
 * @date 2021/8/30
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test1() {
        return "test2";
    }

}
