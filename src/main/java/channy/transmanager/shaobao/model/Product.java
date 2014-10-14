package channy.transmanager.shaobao.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 7872055046179386008L;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
