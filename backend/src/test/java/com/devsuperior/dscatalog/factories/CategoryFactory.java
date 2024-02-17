package com.devsuperior.dscatalog.factories;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.entities.Category;

public class CategoryFactory {
	
	public static Category createNewCategory() {
		Category category = new Category(2L, "Eletrônicos");
		return category;
	}
	
	public static CategoryDTO createNewCategoryDTO() {
		Category category = createNewCategory();
		CategoryDTO categoryDTO = new CategoryDTO(category);
		return categoryDTO;
	}

}
