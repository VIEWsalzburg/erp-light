package at.erp.light.view.dto;


public class ArticleDTO {
	private int articleId;
	private String description;
	private String packagingUnit;
	private double weightpu;
	private String mdd;
	private double pricepu;
	
	public ArticleDTO(){}
	
	public ArticleDTO(int articleId, String description, String packagingUnit,
			double weightpu, String mdd, double pricepu) {
		super();
		this.articleId = articleId;
		this.description = description;
		this.packagingUnit = packagingUnit;
		this.weightpu = weightpu;
		this.mdd = mdd;
		this.pricepu = pricepu;
	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPackagingUnit() {
		return packagingUnit;
	}

	public void setPackagingUnit(String packagingUnit) {
		this.packagingUnit = packagingUnit;
	}

	public double getWeightpu() {
		return weightpu;
	}

	public void setWeightpu(double weightpu) {
		this.weightpu = weightpu;
	}

	public String getMdd() {
		return mdd;
	}

	public void setMdd(String mdd) {
		this.mdd = mdd;
	}

	public double getPricepu() {
		return pricepu;
	}

	public void setPricepu(double pricepu) {
		this.pricepu = pricepu;
	}
	
}
