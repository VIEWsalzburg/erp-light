package at.erp.light.view.dto;


public class OutgoingArticleDTO {
	
	private int outgoingArticleId;
	private ArticleDTO articleDTO;
	private Integer articleNr;	// used for order within an incomingDelivery
	private Integer numberpu;
	
	public OutgoingArticleDTO(){}
	
	public OutgoingArticleDTO(int outgoingArticleId, ArticleDTO articleDTO,
			Integer articleNr, Integer numberpu) {
		super();
		this.outgoingArticleId = outgoingArticleId;
		this.articleDTO = articleDTO;
		this.articleNr = articleNr;
		this.numberpu = numberpu;
	}
	public int getOutgoingArticleId() {
		return outgoingArticleId;
	}
	public void setOutgoingArticleId(int outgoingArticleId) {
		this.outgoingArticleId = outgoingArticleId;
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
	public Integer getNumberpu() {
		return numberpu;
	}
	public void setNumberpu(Integer numberpu) {
		this.numberpu = numberpu;
	}
	
	
}
