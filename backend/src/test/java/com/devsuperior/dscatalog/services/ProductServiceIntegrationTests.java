package com.devsuperior.dscatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.execeptions.ResourceNotFoundException;

@SpringBootTest
@Transactional //garante que cada teste faça rollback no banco
public class ProductServiceIntegrationTests {
	
	@Autowired
	ProductService service;
	@Autowired
	ProductRepository repository;
	
	//Arrange Compartilhada
		private long existingId;
		private long nonExistingId;
		private long countTotalProducts;
		//private Product product;
		//private ProductDTO productDTO;
		//private Page<Product> products;
	
	@BeforeEach
	void setup() throws Exception {
		existingId = 1L;
		nonExistingId = 0L;
	    countTotalProducts = 25L;
	    //product = ProductFactory.createNewProduct();
	    //productDTO = ProductFactory.creteNewProductDTO();
	    //products = new PageImpl<>(List.of(product)); //classe concreta do Page
	}	
	
	@Test
	public void findAllPagedShouldReturnPageProductDTO() {
		//Arrange
		Pageable pageable = PageRequest.of(0, 10);
		//Acting
		Page<ProductDTO> result = service.findAllPaged(pageable);
		//Assert
		Assertions.assertNotNull(result);
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals(0, result.getNumber());
		Assertions.assertEquals(10, result.getSize());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}

	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortByName() {
		//Arrange
		Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
		//Acting
		Page<ProductDTO> result = service.findAllPaged(pageable);
		//Assert
		Assertions.assertFalse(result.isEmpty());
		Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
		Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
		Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
		//Arrange
		Pageable pageable = PageRequest.of(50, 10);
		//Acting
		Page<ProductDTO> result = service.findAllPaged(pageable);
		//Assert
		Assertions.assertTrue(result.isEmpty());
		Assertions.assertEquals(countTotalProducts, result.getTotalElements());
	}
	
	@Test
	public void deleteShouldDeleteResourceWhenIdExists() {
		//Arrange
		//Acting
		service.delete(existingId);
		//Assert
		Assertions.assertEquals(countTotalProducts - 1, repository.count());
	}
	
	@Test
	public void deleteShouldThowsResourceNotFoundExceptionWhenIdDoesNotExist() {
		//Arrange
		//Assert
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
				//Acting
				service.delete(nonExistingId);
		});
	}
	
	
}
