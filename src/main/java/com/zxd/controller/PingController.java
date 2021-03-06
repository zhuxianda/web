package com.zxd.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zxd.dao.MysqlDao;
import com.zxd.model.QueryData;
import com.zxd.service.DatabaseService;
import com.alibaba.fastjson.JSON;

import static com.zxd.utils.JsonHelper.jsonpEntity;;

/**
 * Created by zhuxianda 2017.8.23
 * 第二次提交的测试 文件1
 */
@Controller
public class PingController {
	
	@Autowired
	private DatabaseService databaseService;
    /**
     * 测试连通
     * @param callback
     * @return
     */
    @RequestMapping(value = "ping.do")
    public ResponseEntity<String> ping(String callback){
        return jsonpEntity("pong", callback);
    }
    
    @RequestMapping(value = "testRedis.do")
    public ResponseEntity<String> getBalance(String callback){
        return jsonpEntity(databaseService.getRedis(), callback);
    }
    
    @RequestMapping(value = "testMysql.do")
    public ResponseEntity<String> getMysql(String callback) throws Exception{
        return jsonpEntity(databaseService.getTodoList(), callback);
    }
    
    
    @RequestMapping(value = "saveToList.do")
    public ResponseEntity<String> saveToList(String queryBody) throws Exception{
    	
    	System.out.println(queryBody);
    	
    	QueryData query = JSON.parseObject(queryBody, QueryData.class);
    	
    	System.out.println(query);
    	
        return jsonpEntity(query, null);
    }
}
