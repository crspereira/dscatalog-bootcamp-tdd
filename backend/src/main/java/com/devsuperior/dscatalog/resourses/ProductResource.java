/* Pacote que disponibiliza a camada de controladores(forma de implementar) o REST.
 * Recursos(Conceito) que serão disponibilizados para as aplicações no Front-End
 * consumir, que por sua vez, é diponibilizada pela API do BackEnd.
 * Classe para responder as requisições REST (Recurso REST da "Entidade Product").
 * * @Autowired trata a injeção/instânciação de dependência do Objeto anotado.
 */

package com.devsuperior.dscatalog.resourses;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;

@RestController
@RequestMapping(value = "/products") /* rota dos endpoints */
public class ProductResource {

	// dependencias
	@Autowired
	private ProductService service;

	/* endpoints (CRUD) deste recurso */
	//sem paginação
	/* @GetMapping
	 * public ResponseEntity<List<ProductDTO>> findAll() {
	 * 
	 * List<ProductDTO> list = service.findAll();
	 * 
	 * List<Product> list = new ArrayList<>(); list.add(new Product(1L, "Books"));
	 * list.add(new Product(2L, "Electronics")); //lista mocada
	 * 
	 * return ResponseEntity.ok().body(list); }
	 */
	
	//com paginação
	@GetMapping // com paginação
	public ResponseEntity<Page<ProductDTO>> findAll(Pageable pageable) {
		Page<ProductDTO> list = service.findAllPaged(pageable);
		return ResponseEntity.ok().body(list);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
		ProductDTO dto = service.findById(id);
		return ResponseEntity.ok().body(dto);
	}

	@PostMapping
	public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) {
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();

		return ResponseEntity.created(uri).body(dto);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
		dto = service.update(id, dto);

		return ResponseEntity.ok().body(dto);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<ProductDTO> delete(@PathVariable Long id) {
		service.delete(id);

		return ResponseEntity.noContent().build();
	}
}
