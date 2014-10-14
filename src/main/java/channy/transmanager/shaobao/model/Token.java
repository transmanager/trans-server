package channy.transmanager.shaobao.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import channy.transmanager.shaobao.model.user.User;

@Entity
@Table(name = "tokens")
public class Token implements Serializable {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -5750342206591257702L;
	private String token;
	
	@Id
	@OneToOne
	@JoinColumn(name = "user")
	private User user;
	private Date expiration;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public Date getExpiration() {
		return expiration;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	
	@Override
	public boolean equals(Object obj) {
		return super.equals(obj);
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
