package com.jamir.easycrm.model;


import java.sql.Date;
import java.time.LocalTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Interaction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idinteraction;
		
	@Column(nullable = false)
	private Date date;
	
	@Column(nullable = false)
	private LocalTime time;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private InteractionType type;
	
	@Column()
	private String description;
	
	public Long getIdinteraction() {
		return idinteraction;
	}

	public void setIdinteraction(Long idinteraction) {
		this.idinteraction = idinteraction;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public InteractionType getType() {
		return type;
	}

	public void setType(InteractionType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@ManyToOne()
	@JoinColumn(name = "customer_id")
	private Customer customer;
	
	@ManyToOne()
	@JoinColumn(name = "user_id")
	private User user;
}
