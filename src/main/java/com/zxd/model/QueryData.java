package com.zxd.model;

import java.util.ArrayList;
import java.util.List;

public class QueryData {
	
	@Override
	public String toString() {
		return "QueryData [todos=" + todos + ", show=" + show + "]";
	}
	List<Todo> todos = new ArrayList<Todo>();
	String show = "";
	
	public List<Todo> getTodos() {
		return todos;
	}
	public void setTodos(List<Todo> todos) {
		this.todos = todos;
	}
	public String getShow() {
		return show;
	}
	public void setShow(String show) {
		this.show = show;
	}

	

}
