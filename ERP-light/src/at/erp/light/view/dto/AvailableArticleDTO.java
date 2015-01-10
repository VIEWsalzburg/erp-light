package at.erp.light.view.dto;


public class AvailableArticleDTO {
	
	private int articleId;
	private ArticleDTO articleDTO;
	private double availNumberOfPUs;
	
	public AvailableArticleDTO() {
	}
	
	public AvailableArticleDTO(int articleId, ArticleDTO articleDTO,
			double availNumberOfPUs) {
		super();
		this.articleId = articleId;
		this.articleDTO = articleDTO;
		this.availNumberOfPUs = availNumberOfPUs;
	}

	public int getArticleId() {
		return articleId;
	}

	public void setArticleId(int articleId) {
		this.articleId = articleId;
	}

	public ArticleDTO getArticleDTO() {
		return articleDTO;
	}

	public void setArticleDTO(ArticleDTO articleDTO) {
		this.articleDTO = articleDTO;
	}

	public double getAvailNumberOfPUs() {
		return availNumberOfPUs;
	}

	public void setAvailNumberOfPUs(double availNumberOfPUs) {
		this.availNumberOfPUs = availNumberOfPUs;
	}
	
	
}
