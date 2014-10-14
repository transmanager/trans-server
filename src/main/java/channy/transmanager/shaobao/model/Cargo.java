package channy.transmanager.shaobao.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "cargos")
public class Cargo extends BaseEntity {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -172986109201296890L;
	private String cId;
	
	@OneToOne
	@JoinColumn(name = "product")
	private Product product;
	private int amount;
	private double weight;
	
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	
}
