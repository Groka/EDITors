package com.editors.viberbot.database.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	public User(String viberId, String name, boolean subscribe) {
		super();
		this.viberId = viberId;
		this.name = name;
		this.subscribe = subscribe;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@Column(name = "viberId")
	private String viberId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "subscribe")
	private boolean subscribe;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getViberId() {
		return viberId;
	}

	public void setViberId(String viberId) {
		this.viberId = viberId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getSubscribe() {
		return subscribe;
	}

	public void setSubscribe(boolean subscribe) {
		this.subscribe = subscribe;
	}
}
