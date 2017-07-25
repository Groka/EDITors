package com.editors.viberbot.database.entity;

import jdk.nashorn.internal.objects.annotations.Property;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	
	public User(String viberid, String name, boolean subscribe) {
		super();
		this.viberid = viberid;
		this.name = name;
		this.subscribe = subscribe;
	}

	public User() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
    
	@Column(name = "viberid")
	private String viberid;
	
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

	public String getViberid() {
		return viberid;
	}

	public void setViberid(String viberid) {
		this.viberid = viberid;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", viberid='" + viberid + '\'' +
                ", name='" + name + '\'' +
                ", subscribe=" + subscribe +
                '}';
    }
}
