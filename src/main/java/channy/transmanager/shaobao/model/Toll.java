package channy.transmanager.shaobao.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import channy.transmanager.shaobao.model.order.Order;

@Entity
@Table(name = "tolls")
public class Toll extends BaseEntity {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -3287507133916013061L;

	@ManyToOne
	private Order order;
	
	@OneToOne
	@JoinColumn(name = "entry")
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	private TollStation entry;

	@OneToOne
	@JoinColumn(name = "_exit")
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	private TollStation exit;
	private double amount;
	private boolean isLoading = false;
	
	public Order getOrder() {
		return order;
	}
	public void setOrder(Order order) {
		this.order = order;
	}
	
	public TollStation getEntry() {
		return entry;
	}
	public void setEntry(TollStation entry) {
		this.entry = entry;
	}
	
	public TollStation getExit() {
		return exit;
	}
	public void setExit(TollStation exit) {
		this.exit = exit;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public boolean isLoading() {
		return isLoading;
	}
	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}
}
