package com.editors.viberbot.database.entity;
import java.io.Serializable;
import java.time.LocalTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.web.bind.annotation.ModelAttribute;

@Entity
@Table(name = "rooms")


public class Room implements Serializable {

	public Room(String name, int number, LocalTime startWorkTime, LocalTime endWorkTime) {
		super();
		//this.id = id;
		this.number = number;
		this.name = name;
		this.startWorkTime = startWorkTime;
		this.endWorkTime = endWorkTime;
	}

	public Room(){
		
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private long id;

	@OneToMany
	@Cascade(CascadeType.DELETE)
	List<Reservation> reservations;
	
	public void setId(long id){
		this.id = id;
	}
	
	public long getId() {
		return id;
	}

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

	
	@Column(name = "start_work_time")
	private LocalTime startWorkTime;
	
	public void setStartWorkTime(LocalTime startWorkTime){
		this.startWorkTime = startWorkTime;
	}
	
	public LocalTime getStartWorkTime(){
		return startWorkTime;
	}
	
	@Column(name = "end_work_time")
	private LocalTime endWorkTime;
	
	public void setEndWorkTime(LocalTime endWorkTime){
		this.endWorkTime = endWorkTime;
	}
	
	public LocalTime getEndWorkTime(){
		return endWorkTime;
	}
	
	public String toString(){
		String result = "Room number: " + getNumber() + "\n";
		result += "Room name: " + getName() + "\n";
		result += "Room id: " + getId() + "\n";
		result += "StartWorkTime: " + getStartWorkTime().toString() + "\n"; 
		result += "EndWorkTime: " + getEndWorkTime().toString() + "\n";
		return result;
	}

}
