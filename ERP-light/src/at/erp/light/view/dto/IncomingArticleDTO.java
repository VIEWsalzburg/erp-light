package at.erp.light.view.dto;

/**
 * This class is a DTO representation of the entity IncomingArticle.<br/>
 * It is mainly used to transmit data between the frontend and backend.
 * @author Matthias Schnöll
 *
 */
public class IncomingArticleDTO {
	
	private int incomingArticleId;
	private ArticleDTO articleDTO;
	private Integer articleNr;	// used for order within an incomingDelivery
	private Integer numberpu;
	
	/**
	 * Constructor
	 */
	public IncomingArticleDTO(){}
	
	/**
	 * Constructor
	 * @param incomingArticleId Id of the incomingArticle
	 * @param articleDTO articleDTO object assigned to this incomingArticle
	 * @param articleNr order number of this incomingArticle within the incomingDelivery
	 * @param numberpu number of PUs for the assigned articleDTO
	 */
	public IncomingArticleDTO(int incomingArticleId, ArticleDTO articleDTO,
			Integer articleNr, Integer numberpu) {
		super();
		this.incomingArticleId = incomingArticleId;
		this.articleDTO = articleDTO;
		this.articleNr = articleNr;
		this.numberpu = numberpu;
	}
	
	/**
	 * get the Id of the incoming article
	 * @return Id
	 */
	public int getIncomingArticleId() {
		return incomingArticleId;
	}
	
	/**
	 * set the Id of the incoming article
	 * @param incomingArticleId Id
	 */
	public void setIncomingArticleId(int incomingArticleId) {
		this.incomingArticleId = incomingArticleId;
	}
	
	/**
	 * get the assigned articleDTO object
	 * @return assigned articleDTO object
	 */
	public ArticleDTO getArticleDTO() {
		return articleDTO;
	}
	
	/**
	 * set the articleDTO object
	 * @param articleDTO articleDTO object
	 */
	public void setArticleDTO(ArticleDTO articleDTO) {
		this.articleDTO = articleDTO;
	}
	
	/**
	 * get the order number of the article within the incomingDelivery
	 * @return order number
	 */
	public Integer getArticleNr() {
		return articleNr;
	}
	
	/**
	 * set the order number of the article within the incomingDelivery
	 * @param articleNr order number
	 */
	public void setArticleNr(Integer articleNr) {
		this.articleNr = articleNr;
	}
	
	/**
	 * get the number of PUs for the assigned articleDTO
	 * @return number of PUs
	 */
	public Integer getNumberpu() {
		return numberpu;
	}
	
	/**
	 * set the number of PUs for the assigned articleDTO
	 * @param numberpu number of PUs
	 */
	public void setNumberpu(Integer numberpu) {
		this.numberpu = numberpu;
	}
	
}
