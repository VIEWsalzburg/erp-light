package at.erp.light.view.dto;

/**
 * This class is a DTO representation of the view availarticleindepot.<br/>
 * It is mainly used to transmit data to the frontend.
 * @author Matthias Schnöll
 *
 */
public class AvailableArticleDTO {
	
	private int articleId;
	private ArticleDTO articleDTO;
	private Integer availNumberOfPUs;
	
	/**
	 * Constructor
	 */
	public AvailableArticleDTO() {
	}
	
	/**
	 * Constructor
	 * @param articleId articleId
	 * @param articleDTO articleDTO
	 * @param availNumberOfPUs available number of PU for this article
	 */
	public AvailableArticleDTO(int articleId, ArticleDTO articleDTO,
			Integer availNumberOfPUs) {
		super();
		this.articleId = articleId;
		this.articleDTO = articleDTO;
		this.availNumberOfPUs = availNumberOfPUs;
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
	 * @param articleId articleId
	 */
	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	/**
	 * get the articleDTO
	 * @return articleDTO
	 */
	public ArticleDTO getArticleDTO() {
		return articleDTO;
	}

	/**
	 * set the articleDTO
	 * @param articleDTO articleDTO
	 */
	public void setArticleDTO(ArticleDTO articleDTO) {
		this.articleDTO = articleDTO;
	}

	/**
	 * get the available number of PUs for the article
	 * @return available number of PUs
	 */
	public Integer getAvailNumberOfPUs() {
		return availNumberOfPUs;
	}

	/**
	 * set the available number of PUs for the article
	 * @param availNumberOfPUs available number of PUs
	 */
	public void setAvailNumberOfPUs(Integer availNumberOfPUs) {
		this.availNumberOfPUs = availNumberOfPUs;
	}
	
	
}
