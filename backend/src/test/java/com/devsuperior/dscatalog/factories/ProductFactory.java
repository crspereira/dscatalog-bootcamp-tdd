package com.devsuperior.dscatalog.factories;

import java.time.Instant;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;

public class ProductFactory {
	
	public static Product createNewProduct() {
		Product product = new Product(1L, "Phone", "Good Phone", 800.0, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
		product.getCategories().add(new Category(2L, "Eletrônicos"));
		return product;
	}
	
	public static ProductDTO creteNewProductDTO() {
		Product product = createNewProduct();
		ProductDTO productDTO = new ProductDTO(product, product.getCategories());
		return productDTO;
	}

}
