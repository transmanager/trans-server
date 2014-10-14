package channy.transmanager.shaobao.model.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "schedulers")
public class Scheduler extends User {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -5010760745572007145L;

	/**
	 * Update this value upon receiving a schedule request.
	 */
	private int ordersScheduled = 0;
	
	/**
	 * Data from self-orders are not calculated.
	 * 
	 * When a driver gets an order and takes a photo of it, 
	 * he/she sends it via the client.
	 * 
	 * The timer ticks when the photo is delivered.
	 */
	private int averageResponseTime = -1;

	public int getOrdersScheduled() {
		return ordersScheduled;
	}

	public void setOrdersScheduled(int ordersScheduled) {
		this.ordersScheduled = ordersScheduled;
	}

	public int getAverageResponseTime() {
		return averageResponseTime;
	}

	public void setAverageResponseTime(int averageResponseTime) {
		this.averageResponseTime = averageResponseTime;
	}
	
}
