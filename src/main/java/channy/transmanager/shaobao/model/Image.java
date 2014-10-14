package channy.transmanager.shaobao.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import channy.transmanager.shaobao.model.user.User;

@Entity
@Table(name = "images")
public class Image extends BaseEntity {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -2483488137704849469L;

	@Column(length=16777215)
	private byte[] data;
	private String type;
	private double longitude = -1;
	private double latitude = -1;
	private Date dateTaken;
	private boolean ready = false;
	private String uploadId;
	private Date lastModified;
	
	@OneToOne
	@JoinColumn(name = "author")
	private User author;
	
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public Date getDateTaken() {
		return dateTaken;
	}
	public void setDateTaken(Date dateTaken) {
		this.dateTaken = dateTaken;
	}
	
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public boolean isReady() {
		return ready;
	}
	public void setReady(boolean ready) {
		this.ready = ready;
	}
	public String getUploadId() {
		return uploadId;
	}
	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
}
