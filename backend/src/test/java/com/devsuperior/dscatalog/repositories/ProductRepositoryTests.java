package com.devsuperior.dscatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.factories.ProductFactory;


@DataJpaTest
public class ProductRepositoryTests {
	
	//Injecao Dependencia
	@Autowired
	private ProductRepository productRepository;

	//Arrange Compartilhada
	private long existingId;
	private long nonExistingId;
	private long countTotalProducts;
	
	@BeforeEach
	void setup() throws Exception {
		existingId = 1L;
		nonExistingId = 0L;
		countTotalProducts = 25;
	}
	
	@Test
	public void findByIdShouldReturnOptionalProductNotEmpytWhenIdExists() {
		//Arrange
		//Long nonExistingId = 0L;
		//Acting
		Optional<Product> result = productRepository.findById(existingId);
		//Assert
		Assertions.assertTrue(result.isPresent());
	}
	
	@Test
	public void findByIdShouldReturnOptionalProductEmpytWhenIdDoesNotExists() {
		//Arrange
		//Long existingId = 1L;
		//Acting
		Optional<Product> result = productRepository.findById(nonExistingId);
		//Assert
		Assertions.assertTrue(result.isEmpty());
	}
	
	@Test
	public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
		//Arrange
		Product product = ProductFactory.createNewProduct();
		product.setId(null);
		//Acting
		product = productRepository.save(product);
		//Assert
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1, product.getId());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		//Arrange
		//Long existingId = 1L;
		//Acting
		productRepository.deleteById(existingId);
		Optional<Product> result = productRepository.findById(existingId);
		//Assert
		Assertions.assertFalse(result.isPresent());
	}
	
	@Test
	public void deleteShouldThrowsExceptionWhenIdNotExists() {
		//Arrange
		//Long nonExistingId = 0L;
		//Assert
		Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
			//Acting
			productRepository.deleteById(nonExistingId);
		});
	}

}
