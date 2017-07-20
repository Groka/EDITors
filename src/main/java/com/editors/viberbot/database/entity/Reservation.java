package com.editors.viberbot.database.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservation implements Serializable {
	public Reservation(){
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;
	
	@Column(name = "userId")
	private long userId;
	
	@Column(name = "roomId")
	private long roomId;
	
	@Column(name = "date")
	private LocalDate date;
	
	@Column(name = "time")
	private LocalTime time;

	public long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getRoomId() {
		return roomId;
	}

	public void setRoomId(long roomId) {
		this.roomId = roomId;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", userId=" + userId + ", roomId=" + roomId + ", date=" + date + ", time=" + time
				+ "]";
	}

	public Reservation(long id, long userId, Long roomId, LocalDate date, LocalTime time) {
		super();
		this.id = id;
		this.userId = userId;
		this.roomId = roomId;
		this.date = date;
		this.time = time;
	}

	
}
