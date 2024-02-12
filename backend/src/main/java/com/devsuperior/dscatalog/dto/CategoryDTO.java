/* Pacote reponsável por trafegar os Dados das Entidades entre as camadas
 * de Resources e Service com classes simples.
 * Promove as vantagend de controlar quais dados irão para o Controlador e a API
 * disponibilizá-los para a aplicação e a segurança, pois possilbilita ocultar dados sensíveis
 */
package com.devsuperior.dscatalog.dto;

import java.io.Serializable;

import com.devsuperior.dscatalog.entities.Category;

public class CategoryDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
		//variaveis de instância
		private Long id;
		private String name;
		
		//contrutores
		public CategoryDTO() {
		}
		public CategoryDTO(Long id, String name) {
			this.id = id;
			this.name = name;
		} //contrutor com a entity para facilitar o povamento na instanciação
		public CategoryDTO(Category entity) {
			id = entity.getId();
			name = entity.getName();
		}
		
		//geters e setters
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	
}
