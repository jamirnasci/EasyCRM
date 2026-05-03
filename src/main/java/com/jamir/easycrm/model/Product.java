package com.jamir.easycrm.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idproduct;
    @NotBlank
    @Column(nullable = false)
    private String name;    
    @NotNull(message = "Preço é obrigatório")
    @Positive
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

	@NotNull(message = "Categoria é obrigatória")
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private ProductCategory category;

    @Column()
    private String description;
    @Column()
    private String imgUrl;
    @NotNull(message = "Quantidade é obrigatória")
    @Positive
    @Column(nullable = false)
    private int quantity;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created_at = LocalDateTime.now();
	public Long getIdproduct() {
		return idproduct;
	}
	public void setIdproduct(Long idproduct) {
		this.idproduct = idproduct;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public LocalDateTime getCreated_at() {
		return created_at;
	}
	public void setCreated_at(LocalDateTime created_at) {
		this.created_at = created_at;
	}
	public ProductCategory getCategory() {
		return category;
	}
	public void setCategory(ProductCategory category) {
		this.category = category;
	}
    
}
