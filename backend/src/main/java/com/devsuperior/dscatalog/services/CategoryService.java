/* Pacote que disponibiliza a Camada de Serviço que Gerencia os
 * Resources e Repositories da Entidade Category").
 * @Service/@Component registra esta classe como um componente que participará
 * do sistema de injeção de dependêcia automatizado do Spring".
 * @Autowired trata a injeção/instânciação de dependência do Objeto anotado.
 */
package com.devsuperior.dscatalog.services;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.execeptions.DatabaseException;
import com.devsuperior.dscatalog.services.execeptions.ResourceNotFoundException;

@Service
public class CategoryService {

	// dependecias
	@Autowired
	private CategoryRepository repository;

	// metodos
	//sem paginação
	/* @Transactional(readOnly = true) public List<CategoryDTO> findAll() {
	 * List<Category> list = repository.findAll(); return list.stream().map(x -> new
	 * CategoryDTO(x)).collect(Collectors.toList());
	 * 
	 * // Expressão Lambida - convertendo a lista Category para CategoryDTO
	 * 
	 * List<CategoryDTO> listDto = list.stream().map(x -> new
	 * CategoryDTO(x)).collect(Collectors.toList()); return listDto;
	 * 
	 * 
	 * // Convencional - convertendo a lista Category para CategoryDTO
	 * 
	 * List<CategoryDTO> listDto = new ArrayList<>(); for (Category cat : list) {
	 * listDto.add(new CategoryDTO(cat)); } return listDto;
	 * 
	 * }
	 */
	
	//com paginação
	@Transactional(readOnly = true) //evita o looking do Banco
	public Page<CategoryDTO> findAllPaged(Pageable pageable) {
		Page<Category> list = repository.findAll(pageable);
		return list.map(x -> new CategoryDTO(x));
	}

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		Optional<Category> obj = repository.findById(id);
		Category entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity Not Found!"));
		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		Category entity = new Category();
		entity.setName(dto.getName());
		entity = repository.save(entity);

		return new CategoryDTO(entity);
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = repository.getReferenceById(id);
			entity.setName(dto.getName());
			entity = repository.save(entity);

			return new CategoryDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id Not Found! " + id);
		}
	}

	// Sem @Transaction para possibilitar captura da excessão
	public void delete(Long id) {
		try {
			repository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Id Not Found! " + id);
		} catch (DataIntegrityViolationException e) { //mantem a integridade referencial
			throw new DatabaseException("Integrity Violation!");
		}
	}
}
