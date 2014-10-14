package channy.transmanager.shaobao.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "departments")
public class Department extends BaseEntity {

	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 6458448469160659336L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
