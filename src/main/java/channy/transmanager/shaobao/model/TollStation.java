package channy.transmanager.shaobao.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tollstations")
public class TollStation extends BaseEntity {
	
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -3585637509452504674L;
	private String name;
	private double x = -1;
	private double y = -1;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
}
