/* Pacote reponsável por trafegar os Dados das Entidades entre as camadas
 * de Resources e Service com classes simples.
 * Promove as vantagend de controlar quais dados irão para o Controlador e a API
 * disponibilizá-los para a aplicação e a segurança, pois possilbilita ocultar dados sensíveis
 */
package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.devsuperior.dscatalog.entities.User;

public class UserDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
		//variaveis de instância
		private Long id;
		@Size(min = 3, max = 60, message = "Deve estar entre 3 e 60 Caracteres")
		@NotBlank(message = "Campo Obrigatório")
		private String firstName;
		private String lastName;
		@Email(message = "Entre Com um E-mail Válido")
		private String email;
		
		//dependecias
		private List<RoleDTO> roles = new ArrayList<>();
		
		//contrutores
		public UserDTO() {
		}
		public UserDTO(Long id, String firstName, String lastName, String email) {
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
		}
		public UserDTO(User entity) {
			id = entity.getId();
			firstName = entity.getFirstName();
			lastName = entity.getLastName();
			email = entity.getEmail();
			//metodo eager da classe User já tras a lista de roles
			entity.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
		}
		
		//geters e setters
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getFirstName() {
			return firstName;
		}
		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}
		public String getLastName() {
			return lastName;
		}
		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public List<RoleDTO> getRoles() {
			return roles;
		}
		
}
