package com.zxd.dao;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class MysqlDao {

	@Autowired
	@Qualifier(value = "jdbcTemplate")
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> getTodoLists() {
		String sql = "select * from todolist";
		List<Map<String, Object>> list = this.jdbcTemplate.queryForList(sql);
		return list;
	}
	
	public static void main(String[] args) {
		// ApplicationContext ac = new
		// ClassPathXmlApplicationContext("classpath:spring/adgame_planquery.xml");
	}

}
