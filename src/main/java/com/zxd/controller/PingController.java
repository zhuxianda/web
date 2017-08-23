package com.zxd.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.zxd.utils.JsonHelper.jsonpEntity;;

/**
 * Created by liyuanzhe on 17-3-2.
 */
@Controller
public class PingController {

    /**
     * 测试连通
     * @param callback
     * @return
     */
    @RequestMapping(value = "ping.do")
    public ResponseEntity<String> getBalance(String callback){
        return jsonpEntity("pong", callback);
    }

}
