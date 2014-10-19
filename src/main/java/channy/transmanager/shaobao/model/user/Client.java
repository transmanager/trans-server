package channy.transmanager.shaobao.model.user;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import channy.transmanager.shaobao.data.user.RoleDao;

@Entity
@Table(name = "clients")
public class Client extends User {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 6327912425203329696L;
	private int orderCount = 0;
	private double orderRate = 0; // number/month
	private Date lastOrder;

	public Client() {
		
	}
	
	/**
	 * Workaround to be compatible with Hibernate restrictions
	 * @param placeholder, just a place holder, could be value
	 */
	public Client(int placeholder) {
		RoleDao dao = new RoleDao();
		Role role = dao.getByName("客户");
		if (role == null) {
			role = dao.addClient();
		}
		
		setRole(role);
	}
	
	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public double getOrderRate() {
		return orderRate;
	}

	public void setOrderRate(double orderRate) {
		this.orderRate = orderRate;
	}

	public Date getLastOrder() {
		return lastOrder;
	}

	public void setLastOrder(Date lastOrder) {
		this.lastOrder = lastOrder;
	}
	
	
}
