package channy.transmanager.shaobao.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "places")
public class Place extends BaseEntity {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -8183325996454702665L;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
