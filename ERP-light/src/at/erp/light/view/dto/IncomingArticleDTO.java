package at.erp.light.view.dto;


public class IncomingArticleDTO {
	
	private int incomingArticleId;
	private ArticleDTO articleDTO;
	private Integer articleNr;	// used for order within an incomingDelivery
	private Double numberpu;
	
	public IncomingArticleDTO(){}
	
	public IncomingArticleDTO(int incomingArticleId, ArticleDTO articleDTO,
			Integer articleNr, Double numberpu) {
		super();
		this.incomingArticleId = incomingArticleId;
		this.articleDTO = articleDTO;
		this.articleNr = articleNr;
		this.numberpu = numberpu;
	}
	public int getIncomingArticleId() {
		return incomingArticleId;
	}
	public void setIncomingArticleId(int incomingArticleId) {
		this.incomingArticleId = incomingArticleId;
	}
	public ArticleDTO getArticleDTO() {
		return articleDTO;
	}
	public void setArticleDTO(ArticleDTO articleDTO) {
		this.articleDTO = articleDTO;
	}
	public Integer getArticleNr() {
		return articleNr;
	}
	public void setArticleNr(Integer articleNr) {
		this.articleNr = articleNr;
	}
	public Double getNumberpu() {
		return numberpu;
	}
	public void setNumberpu(Double numberpu) {
		this.numberpu = numberpu;
	}
	
	
}
