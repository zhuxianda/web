package com.zxd.service;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.zxd.dao.MysqlDao;
import com.zxd.dao.RedisDao;

@Component
public class DatabaseService {

	@Autowired
	private MysqlDao mysqlDao;

	@Autowired
	private RedisDao redisDao;
	
	public List<Map<String, Object>> getTodoList() throws Exception {
		//throw new Exception("出价不能为空");
		return mysqlDao.getTodoLists();
	}
	
	public String getRedis() {
		return redisDao.get("hi");
	}
	
	public List<Map<String, Object>> addTodoList() {
		//throw new Exception("出价不能为空");
		return mysqlDao.getTodoLists();
	}
	
	public static void main(String[] args) throws ParseException, InterruptedException {
		// ApplicationContext ac = new
		// ClassPathXmlApplicationContext("spring/adgame_planquery.xml");
	}

}
