/* Pacote reponsável por trafegar os Dados das Entidades entre as camadas
 * de Resources e Service com classes simples.
 * Promove as vantagend de controlar quais dados irão para o Controlador e a API
 * disponibilizá-los para a aplicação e a segurança, pois possilbilita ocultar dados sensíveis
 */
package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class ProductDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	// variaveis de instância
	private Long id;
	@Size(min = 3, max = 60, message = "Deve estar entre 3 e 60 Caracteres")
	@NotBlank(message = "Campo Obrigatório")
	private String name;
	@NotBlank(message = "Campo Requerido")
	private String description;
	@Positive(message = "Valor deve Ser Positivo" )
	private Double price;
	private String imgUrl;
	@PastOrPresent(message = "Data Futura não Permitido")
	private Instant date;
	
	//dependecias
	private List<CategoryDTO> categories = new ArrayList<>();

	// contrutores
	public ProductDTO() {
	}
	public ProductDTO(Long id, String name, String description, Double price, String imgUrl, Instant date) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}
	public ProductDTO(Product entity) {
		id = entity.getId();
		name = entity.getName();
		description = entity.getDescription();
		price = entity.getPrice();
		imgUrl = entity.getImgUrl();
		date = entity.getDate();
	}
	public ProductDTO(Product entity, Set<Category> categories) {
		this(entity);
		categories.forEach(cat -> this.categories.add(new CategoryDTO(cat)));
	}
	

	// geters e setters
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Instant getDate() {
		return date;
	}
	public void setDate(Instant date) {
		this.date = date;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<CategoryDTO> getCategories() {
		return categories;
	}
	
}
