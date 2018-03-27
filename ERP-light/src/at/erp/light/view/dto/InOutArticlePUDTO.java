package at.erp.light.view.dto;

public class InOutArticlePUDTO {

	int organisationId;		// only used for representation in frontend
	int InOutArticleId;		// used to update the new amount of PUs
	int numberPUs;			// number of PUs
	int type;				// specifies the type of the IncomingOrOutgoingArticle (0: Incoming, 1: Outgoing, 2: Depot)
	ArticleDTO articleDTO;	// only used for representation in frontend
	
	public InOutArticlePUDTO(){}
	
	public InOutArticlePUDTO(int organisationId, int inOutArticleId,
			int numberPUs, ArticleDTO articleDTO, int type) {
		super();
		this.organisationId = organisationId;
		InOutArticleId = inOutArticleId;
		this.numberPUs = numberPUs;
		this.type = type;
		this.articleDTO = articleDTO;
	}


	public int getOrganisationId() {
		return organisationId;
	}


	public void setOrganisationId(int organisationId) {
		this.organisationId = organisationId;
	}


	public int getInOutArticleId() {
		return InOutArticleId;
	}


	public void setInOutArticleId(int inOutArticleId) {
		InOutArticleId = inOutArticleId;
	}


	public int getNumberPUs() {
		return numberPUs;
	}


	public void setNumberPUs(int numberPUs) {
		this.numberPUs = numberPUs;
	}


	public ArticleDTO getArticleDTO() {
		return articleDTO;
	}


	public void setArticleDTO(ArticleDTO articleDTO) {
		this.articleDTO = articleDTO;
	}


	public int getType() {
		return type;
	}


	public void setType(int type) {
		this.type = type;
	}
	
}
