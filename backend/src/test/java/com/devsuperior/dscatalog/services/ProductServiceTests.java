package com.devsuperior.dscatalog.services;

import static org.mockito.Mockito.times;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.factories.CategoryFactory;
import com.devsuperior.dscatalog.factories.ProductFactory;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.execeptions.DatabaseException;
import com.devsuperior.dscatalog.services.execeptions.ResourceNotFoundException;

//## Teste Unitário
//   Dependencias devem ser "Mokadas/Simuladas"
@ExtendWith(SpringExtension.class)
public class ProductServiceTests {
	
	//Injeção de Dependencias Mocadas
	@InjectMocks //Cria uma intancia da classe e injeta os mocks que são criados com as anotações @Mock nessa instância
	private ProductService service;
	
	@Mock //cria um objeto simulando a injeção de dependencia
	private ProductRepository productRepository;
	@Mock //cria um objeto simulando a injeção de dependencia
	private CategoryRepository categoryRepository;
	
	//Arrange Compartilhada
		private long existingId;
		private long nonExistingId;
		private long dependentId;
		private long categoryExistingId;
		private Product product;
		private Product newProduct;
		private ProductDTO productDTO;
		private Page<Product> products;
		private Category category;
		
		@BeforeEach
		void setup() throws Exception {
			existingId = 1L;
			nonExistingId = 0L;
		    dependentId = 2L;
		    categoryExistingId = 2L;
		    product = ProductFactory.createNewProduct();
		    productDTO = ProductFactory.creteNewProductDTO();
		    products = new PageImpl<>(List.of(product)); //classe concreta do Page
		    newProduct = ProductFactory.createNewProduct();
		    category = CategoryFactory.createNewCategory();
		    
		// ## Configurando os comportamentos simulados do Product Repository
		    Mockito.when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
		    Mockito.when(productRepository.findById(nonExistingId)).thenReturn(Optional.empty());
		    
		    Mockito.when(productRepository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(products);
		    
		    Mockito.when(productRepository.getReferenceById(existingId)).thenReturn(product);
		    Mockito.when(productRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
		    
		    Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
		    Mockito.when(categoryRepository.getReferenceById(existingId)).thenThrow(EntityNotFoundException.class);
		    
		    Mockito.when(productRepository.save(product)).thenReturn(newProduct);
		    //Mockito.when(productRepository.save(ArgumentMatchers.any())).thenReturn(newProduct);
		    
		    Mockito.doNothing().when(productRepository).deleteById(existingId);
			Mockito.doThrow(EmptyResultDataAccessException.class).when(productRepository).deleteById(nonExistingId);
			Mockito.doThrow(DataIntegrityViolationException.class).when(productRepository).deleteById(dependentId);
		}
		
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		//Arrange
		//Acting
		ProductDTO result = service.findById(existingId);
		//Assert
		Assertions.assertNotNull(result);
		Mockito.verify(productRepository, Mockito.times(1)).findById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowsResourceNotFoundExceptionWhenIdDoesNotExist() {
		//Arrange
		//Assert
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			service.findById(nonExistingId);
		});
		Mockito.verify(productRepository, Mockito.times(1)).findById(nonExistingId);
	}
	
	@Test
	public void findAllPagedShouldReturnPagedProductList() {
		//Arrange
		Pageable pageable = PageRequest.of(0, 10);
		//Acting
		Page<ProductDTO> result = service.findAllPaged(pageable);
		//Assert
		Assertions.assertNotNull(result);
		Mockito.verify(productRepository, Mockito.times(1)).findAll(pageable);
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() {
		//Arrange
		//Acting
		ProductDTO result = service.update(existingId, productDTO);
		//Assert
		Assertions.assertNotNull(result);
		Mockito.verify(productRepository, Mockito.times(1)).getReferenceById(existingId);
		Mockito.verify(productRepository, Mockito.times(1)).save(product);
		Mockito.verify(categoryRepository, Mockito.times(1)).getReferenceById(categoryExistingId);
	}
	
	@Test
	public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() {
		//Arrange
		//Assert
		Assertions.assertThrows(ResourceNotFoundException.class, () -> {
			//Acting
			service.update(nonExistingId, productDTO);
		});
		Mockito.verify(productRepository, Mockito.times(1)).getReferenceById(nonExistingId);
	}
	
	@Test 
	public void deleteShouldNothingWhenIdExists() { //não conhece o comportamento do productRepository
		//Arrange
		
		//Assert
		Assertions.assertDoesNotThrow(() -> {
			//Acting
			service.delete(existingId);
		});
		Mockito.verify(productRepository, times(1)).deleteById(existingId);
	}
	
	@Test
	public void deleteShouldThowsDatabaseExceptionWhenDependsId() {
		//Arrange
		
		//Assert
		Assertions.assertThrows(DatabaseException.class, () -> { //execpation da camada de servico
			//Acting
			service.delete(dependentId);
		});
		Mockito.verify(productRepository, times(1)).deleteById(dependentId);
	}
	
	@Test
	public void deleteShouldThowsResourceNotFoundExceptionWhenIdDoesNotExist() {
		//Arrange
		
		//Assert
		Assertions.assertThrows(ResourceNotFoundException.class, () -> { //execpation da camada de servico
			//Acting
			service.delete(nonExistingId);
		});
		Mockito.verify(productRepository, times(1)).deleteById(nonExistingId);
	}
	
}
