package channy.transmanager.shaobao.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "ores")
public class Ore extends BaseEntity {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 3708438173282515025L;
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
