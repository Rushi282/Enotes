package com.enote.enums;

public enum TodoPriority {

	LOW(1,"Low"), NORMAL(2,"Normal"), HIGH(3,"High");
	
	private Integer id;
	
	private String name;
	
	private TodoPriority(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
