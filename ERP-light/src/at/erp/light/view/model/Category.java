package at.erp.light.view.model;

// Generated 22.11.2014 18:02:50 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * Category generated by hbm2java
 */
@Entity
@Table(name = "category", schema = "public")
public class Category implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1730408850433611110L;
	private int categoryId;
	private String category;
	private String description;

	public Category() {
	}

	public Category(int categoryId, String category, String description) {
		this.categoryId = categoryId;
		this.category = category;
		this.description = description;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="gen_category_id")
	@SequenceGenerator(name="gen_category_id", sequenceName="category_category_id_seq", allocationSize=1)
	@Column(name = "category_id", unique = true, nullable = false)
	public int getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	@Column(name = "category", nullable = false, length = 100)
	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name = "description", length = 1024)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
