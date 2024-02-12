/* Pacote reponsável por trafegar os Dados das Entidades entre as camadas
 * de Resources e Service com classes simples.
 * Promove as vantagend de controlar quais dados irão para o Controlador e a API
 * disponibilizá-los para a aplicação e a segurança, pois possilbilita ocultar dados sensíveis
 */
package com.devsuperior.dscatalog.dto;

import java.io.Serializable;

import com.devsuperior.dscatalog.entities.Role;

public class RoleDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
		//variaveis de instância
		private Long id;
		private String authority;
		
		//contrutores
		public RoleDTO() {
		}
		public RoleDTO(Long id, String authority) {
			this.id = id;
			this.authority = authority;
		}
		public RoleDTO(Role entity) {
			id = entity.getId();
			authority = entity.getAuthority();
		}

		//geters e setters
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getAuthority() {
			return authority;
		}
		public void setAuthority(String authority) {
			this.authority = authority;
		}
}
