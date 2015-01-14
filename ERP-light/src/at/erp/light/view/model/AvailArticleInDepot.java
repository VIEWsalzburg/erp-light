package at.erp.light.view.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;




@Entity
@Table(name = "availarticleindepot", schema = "public")
public class AvailArticleInDepot implements java.io.Serializable {

	private int articleId;
	private Article article;
	private double availNumberOfPUs;
	
	public AvailArticleInDepot() {
		
	}
	
	@Id
	@Column(name = "article_id")
	public int getArticleId() {
		return this.articleId;
	}
	
	public void setArticleId(int articleId) {
		this.articleId = articleId;
		// dont so anything ... this is a DB VIEW
	}
	
	@OneToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "article_id", referencedColumnName = "article_id")
	public Article getArticle() {
		return this.article;
	}
	
	public void setArticle(Article article) {
		// dont do anything ... this is a Database VIEW
		this.article = article;
	}
	
	
	
	@Column(name = "availnumberofpus")
	public double getAvailNumberOfPUs() {
		return this.availNumberOfPUs;
	}
	
	public void setAvailNumberOfPUs(double availNumberOfPUs) {
		// dont do anything ... this is a Database VIEW
		this.availNumberOfPUs = availNumberOfPUs;
	}
	
	
}