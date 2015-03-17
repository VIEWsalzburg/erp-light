package at.erp.light.view.dto;

/**
 * This class is a DTO representation of the entity Category.<br/>
 * It is mainly used to transmit data between the frontend and backend.
 * @author Matthias Schnöll
 *
 */
public class CategoryDTO {
	
	private int categoryId;
	private String category;
	private String description;

	/**
	 * Constructor
	 */
	public CategoryDTO() {
	}

	/**
	 * Constructor
	 * @param categoryId Id of the category
	 * @param category name of the category
	 * @param description long description of the category
	 */
	public CategoryDTO(int categoryId, String category, String description) {
		this.categoryId = categoryId;
		this.category = category;
		this.description = description;
	}

	/**
	 * get the Id of the category
	 * @return Id
	 */
	public int getCategoryId() {
		return categoryId;
	}

	/**
	 * set the Id for the category
	 * @param categoryId Id
	 */
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * get the name of the category
	 * @return name of the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * set the name of the category
	 * @param category name of the category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * get the description of the category
	 * @return description of the category
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * set the description of the category
	 * @param description description of the category
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
