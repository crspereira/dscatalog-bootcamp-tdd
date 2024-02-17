package com.devsuperior.dscatalog.resources;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.factories.ProductFactory;
import com.devsuperior.dscatalog.resourses.ProductResource;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.execeptions.DatabaseException;
import com.devsuperior.dscatalog.services.execeptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

	// Mocka as requisições
	@Autowired
	private MockMvc mockMvc;

	// Mocka as dependencias com alguns beans de sistema
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper objectMapper;

	// Arrange Compartilhada
	private long existingId;
	private long nonExistingId;
	private long dependentId;
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> pageProductDTO; // PageImpl Objeto concreto da Interface Page

	@BeforeEach
	void setup() throws Exception {
		existingId = 1L;
		nonExistingId = 0L;
		dependentId = 2L;
		productDTO = ProductFactory.creteNewProductDTO();
		pageProductDTO = new PageImpl<>(List.of(productDTO));

		// ## Configurando os comportamentos simulados do ProductService
		Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(pageProductDTO);

		Mockito.when(service.findById(existingId)).thenReturn(productDTO);
		Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

		Mockito.when(service.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenReturn(productDTO);
		Mockito.when(service.update(ArgumentMatchers.eq(existingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);
		
		doNothing().when(service).delete(existingId);
		doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DatabaseException.class).when(service).delete(dependentId);
	}

	@Test
	public void findAllShouldReturnPageProductDTO() throws Exception {
		// Arrange
		Pageable pageable = PageRequest.of(0, 10);
		// Acting
		ResultActions result = mockMvc.perform(get("/products")
				.accept(MediaType.APPLICATION_JSON));
		// Assert
		result.andExpect(status().isUnauthorized());
		Mockito.verify(service, Mockito.times(0)).findAllPaged(pageable);
	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {
		// Arrange
		// Acting
		ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
				.accept(MediaType.APPLICATION_JSON));
		// Assert
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		Mockito.verify(service, Mockito.times(1)).findById(existingId);
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesExist() throws Exception {
		// Arrange
		// Acting
		ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId)
				.accept(MediaType.APPLICATION_JSON));
		// Assert
		result.andExpect(status().isNotFound());
		Mockito.verify(service, Mockito.times(0)).findById(nonExistingId);
	}

	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		// Arrange
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		// Acting
		ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		// Assert
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		Mockito.verify(service, Mockito.times(1)).update(existingId, productDTO);
	}

	@Test
	public void updateShouldReturnNotFoundWhenIdDoesExist() throws Exception {
		// Arrange
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		// Acting
		ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
				.content(jsonBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON));
		// Assert
		result.andExpect(status().isNotFound());
		Mockito.verify(service, Mockito.times(0)).update(nonExistingId, productDTO);

	}

}
