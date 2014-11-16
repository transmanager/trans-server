package channy.transmanager.shaobao.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@Table(name = "expenses")
public class Expenses extends BaseEntity {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 1870173710777145916L;
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
