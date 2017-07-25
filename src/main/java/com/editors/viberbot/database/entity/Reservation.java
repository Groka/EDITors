package com.editors.viberbot.database.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "reservations")
public class Reservation {
	public Reservation(User user, Room room, LocalDate date, LocalTime time) {
		this.user = user;
		this.room = room;
		this.date = date;
		this.time = time;
	}

	public Reservation(){
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;
	
	@OneToOne
	private User user;
	
	@OneToOne
	private Room room;
	
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	@Override
	public String toString() {
		return "Reservation [id=" + id + ", user=" + user + ", room=" + room + ", date=" + date + ", time=" + time
				+ "]";
	}
	

	
}
