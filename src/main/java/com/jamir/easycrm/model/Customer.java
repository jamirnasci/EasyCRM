package com.jamir.easycrm.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idcustomer;

	@NotBlank(message = "Nome é obrigatório")
	@Column(nullable = false)
	private String name;

	@NotBlank(message = "CPF é obrigatório")
	@Column(nullable = false, unique = true)
	private String cpf;

	@NotBlank(message = "Telefone é obrigatório")
	@Column(nullable = false, unique = true)
	private String phone;

	@NotBlank(message = "Email é obrigatório")
	@Column(nullable = false, unique = true)
	private String email;

	@Column()
	private String description;

	@NotNull(message = "Status é obrigatório")
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private CustomerStatus status = CustomerStatus.LEAD;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime created_at = LocalDateTime.now();

	public Long getIdcustomer() {
		return idcustomer;
	}

	public void setIdcustomer(Long idcustomer) {
		this.idcustomer = idcustomer;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getPhone() {
		return phone;
	}

	public CustomerStatus getStatus() {
		return status;
	}

	public void setStatus(CustomerStatus status) {
		this.status = status;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getCreated_at() {
		return created_at;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}

}
