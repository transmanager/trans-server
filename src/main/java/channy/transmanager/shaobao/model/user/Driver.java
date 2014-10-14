package channy.transmanager.shaobao.model.user;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import channy.transmanager.shaobao.model.vehicle.Truck;

@Entity
@Table(name = "drivers")
public class Driver extends User {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -3419112915334734620L;

	public Driver() {
		super();
		//TODO: set its role
	}

	@OneToOne
	@JoinColumn(name = "truck")
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	private Truck truck;
	private int ordersCompleted;
	private double distanceTravelled;
	private double cargoDelivered;
	private double oreDelivered;
	private double weightCheated;
	private double fuelUsed;
	
	public Truck getTruck() {
		return truck;
	}
	public void setTruck(Truck truck) {
		this.truck = truck;
	}
	public int getOrdersCompleted() {
		return ordersCompleted;
	}
	public void setOrdersCompleted(int ordersCompleted) {
		this.ordersCompleted = ordersCompleted;
	}
	public double getDistanceTravelled() {
		return distanceTravelled;
	}
	public void setDistanceTravelled(double distanceTravelled) {
		this.distanceTravelled = distanceTravelled;
	}
	public double getCargoDelivered() {
		return cargoDelivered;
	}
	public void setCargoDelivered(double cargoDelivered) {
		this.cargoDelivered = cargoDelivered;
	}
	public double getOreDelivered() {
		return oreDelivered;
	}
	public void setOreDelivered(double oreDelivered) {
		this.oreDelivered = oreDelivered;
	}
	public double getWeightCheated() {
		return weightCheated;
	}
	public void setWeightCheated(double weightCheated) {
		this.weightCheated = weightCheated;
	}
	public double getFuelUsed() {
		return fuelUsed;
	}
	public void setFuelUsed(double fuelUsed) {
		this.fuelUsed = fuelUsed;
	}
}
