package com.jamir.easycrm.model;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class Sale {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idsale;

	@NotNull(message = "Forma de pagamento é obrigatória")
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private PaymentMethod paymentMethod;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private SaleStatus status = SaleStatus.EM_NEGOCIACAO;

	@Positive(message = "Quantidade deve ser positiva")
	@NotNull(message = "Quantidade é obrigatória")
	@Column(nullable = false)
	private int quantity;

	@Positive(message = "Preço total deve ser positivo")
	@NotNull(message = "Total é obrigatório")
	@Column(nullable = false)
	private BigDecimal total;
	
	@Column()
	private LocalDate date = LocalDate.now();

	@NotNull(message = "Cliente é obrigatório")
	@ManyToOne()
	@JoinColumn(name = "customer_id")
	private Customer customer;

	@ManyToOne()
	@JoinColumn(name = "user_id")
	private User user;

	@NotNull(message = "Produto é obrigatório")
	@ManyToOne()
	@JoinColumn(name = "product_id")
	private Product product;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt = LocalDateTime.now();

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public Long getIdsale() {
		return idsale;
	}

	public void setIdsale(Long idsale) {
		this.idsale = idsale;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public SaleStatus getStatus() {
		return status;
	}

	public void setStatus(SaleStatus status) {
		this.status = status;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public LocalDateTime getCreated_at() {
		return createdAt;
	}

	public void setCreated_at(LocalDateTime created_at) {
		this.createdAt = created_at;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	@Override
	public String toString() {
		return "Sale [idsale=" + idsale + ", paymentMethod=" + paymentMethod + ", status=" + status + ", quantity="
				+ quantity + ", total=" + total + ", date=" + date + ", customer=" + customer + ", user=" + user
				+ ", product=" + product + ", createdAt=" + createdAt + "]";
	}
	
}
