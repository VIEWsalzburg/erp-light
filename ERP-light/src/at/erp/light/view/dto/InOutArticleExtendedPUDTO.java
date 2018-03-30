package at.erp.light.view.dto;

public class InOutArticleExtendedPUDTO extends InOutArticlePUDTO{
	
	String outgoingDate;
	
	public InOutArticleExtendedPUDTO() {}
	
	public InOutArticleExtendedPUDTO(int organisationId, String outgoingDate,int inOutArticleId,
			int numberPUs, ArticleDTO articleDTO, int type) {
		super(organisationId,inOutArticleId,numberPUs,articleDTO,type);
		this.outgoingDate = outgoingDate;
	}
	
	public String getOutgoingDate()
	{
		return this.outgoingDate;
	}
	public void setOutgoingDate(String outgoingDate)
	{
		this.outgoingDate = outgoingDate;
	}

}
