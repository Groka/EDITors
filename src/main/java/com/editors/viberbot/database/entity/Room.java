package com.editors.viberbot.database.entity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "rooms")

public class Room implements Serializable {

	private static final long serialVersionUID = -3009157732242241606L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;

	@Column(name = "number")
	private int number;
	
	public void setNumber(int number){
		this.number = number;
	}
	
	public int getNumber(){
		return number;
	}
	
	@Column(name = "name")
	private String name;

	public void setName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}

	public Room(int i, String name) {
		this.number = i;
		this.name = name;
	}

}
