package at.erp.light.view.dto;

/**
 * This class is a DTO representation of the entity Article.<br/>
 * It is mainly used to transmit data between the frontend and backend.
 * @author Matthias Schnöll
 *
 */
public class ArticleDTO {
	private int articleId;
	private String description;
	private String packagingUnit;
	private double weightpu;
	private String mdd;
	private double pricepu;
	private int delivererId;	// represents the OrganisationId where the article is from
	
	/**
	 * Constructor
	 */
	public ArticleDTO(){}
	
	/**
	 * Constructor
	 * @param articleId Id
	 * @param description Description
	 * @param packagingUnit Type of packaging Unit
	 * @param weightpu weight per PU
	 * @param mdd mdd of the article
	 * @param pricepu price per PU
	 * @param delivererId Id of the deliverer
	 */
	public ArticleDTO(int articleId, String description, String packagingUnit,
			double weightpu, String mdd, double pricepu, int delivererId) {
		super();
		this.articleId = articleId;
		this.description = description;
		this.packagingUnit = packagingUnit;
		this.weightpu = weightpu;
		this.mdd = mdd;
		this.pricepu = pricepu;
		this.delivererId = delivererId;
	}

	/**
	 * get the articleId
	 * @return articleId
	 */
	public int getArticleId() {
		return articleId;
	}

	/**
	 * set the articleId
	 * @param articleId
	 */
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	/**
	 * get the description
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * set the description
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * get the packagingUnit
	 * @return packagingUnit
	 */
	public String getPackagingUnit() {
		return packagingUnit;
	}

	/**
	 * set the packagingUnit
	 * @param packagingUnit
	 */
	public void setPackagingUnit(String packagingUnit) {
		this.packagingUnit = packagingUnit;
	}

	/**
	 * get the weightpu
	 * @return weightpu
	 */
	public double getWeightpu() {
		return weightpu;
	}

	/**
	 * set the weightpu
	 * @param weightpu
	 */
	public void setWeightpu(double weightpu) {
		this.weightpu = weightpu;
	}

	/**
	 * get the mdd
	 * @return mdd
	 */
	public String getMdd() {
		return mdd;
	}

	/**
	 * set the mdd
	 * @param mdd
	 */
	public void setMdd(String mdd) {
		this.mdd = mdd;
	}

	/**
	 * get the pricepu
	 * @return pricepu
	 */
	public double getPricepu() {
		return pricepu;
	}

	/**
	 * set the pricepu
	 * @param pricepu
	 */
	public void setPricepu(double pricepu) {
		this.pricepu = pricepu;
	}

	/**
	 * get the delivererId
	 * @return delivererId
	 */
	public int getDelivererId() {
		return delivererId;
	}

	/**
	 * set the delivererId
	 * @param delivererId
	 */
	public void setDelivererId(int delivererId) {
		this.delivererId = delivererId;
	}
	
}
