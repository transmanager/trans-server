package channy.transmanager.shaobao.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "fines")
public class Fine extends BaseEntity {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 3648427875204414029L;
	
	private String type;
	private double amount;
	private Date dateExpensed;
	
	public Date getDateExpensed() {
		return dateExpensed;
	}
	public void setDateExpensed(Date dateExpensed) {
		this.dateExpensed = dateExpensed;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
}
