package channy.transmanager.shaobao.model.vehicle;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import channy.transmanager.shaobao.model.BaseEntity;
import channy.transmanager.shaobao.model.user.Driver;

@Entity
@Table(name = "trucks")
public class Truck extends BaseEntity {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 8067304599386184247L;
	private String plate;
	
	@Enumerated(EnumType.STRING)
	private TruckStatus status = TruckStatus.Idle;
	private int age = -1; // In days
	private int odometer = 0;
	private Date lastOrder;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "motorcade")
	private Motorcade motorcade;
	
	@OneToOne(fetch = FetchType.EAGER)
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "driver")
	private Driver driver;
	private double x = -1;
	private double y = -1;
	private Date lastCoordinateUpdated;
	
	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public TruckStatus getStatus() {
		return status;
	}

	public void setStatus(TruckStatus status) {
		this.status = status;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public int getOdometer() {
		return odometer;
	}

	public void setOdometer(int odometer) {
		this.odometer = odometer;
	}

	public Date getLastOrder() {
		return lastOrder;
	}

	public void setLastOrder(Date lastOrder) {
		this.lastOrder = lastOrder;
	}

	
	public Motorcade getMotorcade() {
		return motorcade;
	}

	public void setMotorcade(Motorcade motorcade) {
		this.motorcade = motorcade;
	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
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

	public Date getLastCoordinateUpdated() {
		return lastCoordinateUpdated;
	}

	public void setLastCoordinateUpdated(Date lastCoordinateUpdated) {
		this.lastCoordinateUpdated = lastCoordinateUpdated;
	}
}
