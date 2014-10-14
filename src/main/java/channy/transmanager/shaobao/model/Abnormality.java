package channy.transmanager.shaobao.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Table(name = "abnormalities")
public class Abnormality extends BaseEntity {
	@Transient
	private static final long serialVersionUID = 7889184075203587305L;
	
	private String type;
	
	private Date dateOccurred;

	@OneToMany(fetch = FetchType.EAGER)
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "abnormality")
	private List<Image> images;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getDateOccurred() {
		return dateOccurred;
	}

	public void setDateOccurred(Date dateOccurred) {
		this.dateOccurred = dateOccurred;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}
}
