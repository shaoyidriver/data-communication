package com.sy.controller;

import com.sy.common.R;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/decrypt")
public class Decrypt {
    @PostMapping("base")
    public R base(Map params) {
        return R.ok(R.OK_STRING, params);
    }
}
