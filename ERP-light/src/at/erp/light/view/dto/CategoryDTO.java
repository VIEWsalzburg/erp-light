package at.erp.light.view.dto;

public class CategoryDTO {
	
	private int categoryId;
	private String category;
	private String description;

	public CategoryDTO() {
	}

	public CategoryDTO(int categoryId, String category, String description) {
		this.categoryId = categoryId;
		this.category = category;
		this.description = description;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
