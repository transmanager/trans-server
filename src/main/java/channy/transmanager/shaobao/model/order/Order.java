package channy.transmanager.shaobao.model.order;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import channy.transmanager.shaobao.model.Abnormality;
import channy.transmanager.shaobao.model.BaseEntity;
import channy.transmanager.shaobao.model.Cargo;
import channy.transmanager.shaobao.model.Expenses;
import channy.transmanager.shaobao.model.Place;
import channy.transmanager.shaobao.model.Image;
import channy.transmanager.shaobao.model.Ore;
import channy.transmanager.shaobao.model.Toll;
import channy.transmanager.shaobao.model.user.Client;
import channy.transmanager.shaobao.model.user.Driver;
import channy.transmanager.shaobao.model.user.User;
import channy.transmanager.shaobao.model.vehicle.Truck;

@Entity
@Table(name = "orders")
//@SecondaryTables({
//	@SecondaryTable(name = "places", pkJoinColumns = {@PrimaryKeyJoinColumn(name = "place", referencedColumnName = "id")})
//})
public class Order extends BaseEntity {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -8332528136646044160L;

	private String dId;
	
	private String cId; // Use ',' to separate multiple instances.
	
	@OneToMany(fetch = FetchType.EAGER)
	@Cascade(CascadeType.SAVE_UPDATE)
	@JoinColumn(name = "order_id")
	private List<Image> image;
	
	private String oId;

	@OneToOne
	@JoinColumn(name = "scheduler")
	private User scheduler;
	
	@OneToOne
	//@Cascade(value = {CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "truck")
	private Truck truck;
	
	@Enumerated(EnumType.STRING)
	private OrderType orderType;
	
	@OneToOne
	@JoinColumn(name = "client")
	private Client client;
	
	@OneToOne
	//@Cascade(value = {CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "driver")
	private Driver driver;

	@OneToOne
	@JoinColumn(name = "cargoSource")
	private Place cargoSource;
	
	@OneToOne
	@JoinColumn(name = "cargoDestination")
	private Place cargoDestination;
	
	@OneToOne
	@JoinColumn(name = "oreSource")
	private Place oreSource;
	
	@OneToOne
	@JoinColumn(name = "oreDestination")
	private Place oreDestination;
	
	@OneToMany(fetch = FetchType.EAGER)
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "order_id")
	private List<Cargo> cargo;
	
	private double cargoWeight;
	private double paidCargoWeight;
	private double finalCargoWeight;
	private double cargoToll;
	private double calculatedCargoDelta;

	@OneToOne
	@JoinColumn(name = "ore")
	private Ore ore;
	private double oreWeight;
	private double paidOreWeight;
	private double finalOreWeight;
	private double oreToll;
	private double calculatedOreDelta;
	
	private Date dateDeparted;
	private Date dateArrived;
	private Date dateReturn;
	private Date dateReturned;
	
	@OneToMany(fetch = FetchType.EAGER)
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "order_id")
	private List<Expenses> expenses;
	
	@OneToMany(fetch = FetchType.EAGER)
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "order_id")
	private List<Abnormality> abnormalities;
	
	private boolean cargoVerified = false;
	private boolean expensesVerified = false;
	private boolean clear = false;
	
	@OneToOne
	@JoinColumn(name = "cargoVerifiedBy")
	private User cargoVerifiedBy;
	private Date dateCargoVerified;
	
	@OneToOne
	@JoinColumn(name = "expensesVerifiedBy")
	private User expensesVerifiedBy;
	private Date dateExpensesVerified;
	
	@OneToOne
	@JoinColumn(name = "verifiedBy")
	private User verifiedBy;
	private Date dateVerified;
	
	@Enumerated(EnumType.STRING)
	private OrderStatus status = OrderStatus.New;
	
	private double odometerStart;
	private double odometerEnd;
	private double distance;
	private double fuelUsed;
	
	@OneToMany(fetch = FetchType.EAGER)
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "order_id")
	private List<Toll> tolls;
	
	public String getoId() {
		return oId;
	}
	public void setoId(String oId) {
		this.oId = oId;
	}
	
	public String getdId() {
		return dId;
	}
	public void setdId(String dId) {
		this.dId = dId;
	}
	
	public String getcId() {
		return cId;
	}
	public void setcId(String cId) {
		this.cId = cId;
	}
	
	public List<Image> getImage() {
		return image;
	}
	public void setImage(List<Image> image) {
		this.image = image;
	}
	
	public User getScheduler() {
		return scheduler;
	}
	public void setScheduler(User scheduler) {
		this.scheduler = scheduler;
	}
	
	public Truck getTruck() {
		return truck;
	}
	public void setTruck(Truck truck) {
		this.truck = truck;
	}
	
	public OrderType getOrderType() {
		return orderType;
	}
	public void setOrderType(OrderType orderType) {
		this.orderType = orderType;
	}
	
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	
	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	
	public Place getCargoSource() {
		return cargoSource;
	}
	public void setCargoSource(Place cargoSource) {
		this.cargoSource = cargoSource;
	}

	public Place getCargoDestination() {
		return cargoDestination;
	}
	public void setCargoDestination(Place cargoDestination) {
		this.cargoDestination = cargoDestination;
	}

	public Place getOreSource() {
		return oreSource;
	}
	public void setOreSource(Place oreSource) {
		this.oreSource = oreSource;
	}

	public Place getOreDestination() {
		return oreDestination;
	}
	public void setOreDestination(Place oreDestination) {
		this.oreDestination = oreDestination;
	}
	
	public List<Cargo> getCargo() {
		return cargo;
	}
	public void setCargo(List<Cargo> cargo) {
		this.cargo = cargo;
	}
	public double getCargoWeight() {
		return cargoWeight;
	}
	public void setCargoWeight(double cargoWeight) {
		this.cargoWeight = cargoWeight;
	}
	public double getPaidCargoWeight() {
		return paidCargoWeight;
	}
	public void setPaidCargoWeight(double paidCargoWeight) {
		this.paidCargoWeight = paidCargoWeight;
	}
	public double getCargoToll() {
		return cargoToll;
	}
	public void setCargoToll(double cargoToll) {
		this.cargoToll = cargoToll;
	}
	public double getCalculatedCargoDelta() {
		return calculatedCargoDelta;
	}
	public void setCalculatedCargoDelta(double calculatedCargoDelta) {
		this.calculatedCargoDelta = calculatedCargoDelta;
	}

	public Ore getOre() {
		return ore;
	}
	public void setOre(Ore ore) {
		this.ore = ore;
	}
	public double getOreWeight() {
		return oreWeight;
	}
	public void setOreWeight(double oreWeight) {
		this.oreWeight = oreWeight;
	}
	public double getPaidOreWeight() {
		return paidOreWeight;
	}
	public void setPaidOreWeight(double paidOreWeight) {
		this.paidOreWeight = paidOreWeight;
	}
	public double getFinalOreWeight() {
		return finalOreWeight;
	}
	public void setFinalOreWeight(double finalOreWeight) {
		this.finalOreWeight = finalOreWeight;
	}
	public double getOreToll() {
		return oreToll;
	}
	public void setOreToll(double oreToll) {
		this.oreToll = oreToll;
	}
	public double getCalculatedOreDelta() {
		return calculatedOreDelta;
	}
	public void setCalculatedOreDelta(double calculatedOreDelta) {
		this.calculatedOreDelta = calculatedOreDelta;
	}
	public Date getDateDeparted() {
		return dateDeparted;
	}
	public void setDateDeparted(Date dateDeparted) {
		this.dateDeparted = dateDeparted;
	}
	public Date getDateArrived() {
		return dateArrived;
	}
	public void setDateArrived(Date dateArrived) {
		this.dateArrived = dateArrived;
	}
	public Date getDateReturn() {
		return dateReturn;
	}
	public void setDateReturn(Date dateReturn) {
		this.dateReturn = dateReturn;
	}
	public Date getDateReturned() {
		return dateReturned;
	}
	public void setDateReturned(Date dateReturned) {
		this.dateReturned = dateReturned;
	}
	
	public List<Expenses> getExpenses() {
		return expenses;
	}
	public void setExpenses(List<Expenses> expenses) {
		this.expenses = expenses;
	}
	
	public List<Abnormality> getAbnormalities() {
		return abnormalities;
	}
	public void setAbnormalities(List<Abnormality> abnormalities) {
		this.abnormalities = abnormalities;
	}
	public boolean isCargoVerified() {
		return cargoVerified;
	}
	public void setCargoVerified(boolean cargoVerified) {
		this.cargoVerified = cargoVerified;
	}
	public boolean isExpensesVerified() {
		return expensesVerified;
	}
	public void setExpensesVerified(boolean expensesVerified) {
		this.expensesVerified = expensesVerified;
	}
	public boolean isClear() {
		return clear;
	}
	public void setClear(boolean clear) {
		this.clear = clear;
	}
	
	public User getCargoVerifiedBy() {
		return cargoVerifiedBy;
	}
	public void setCargoVerifiedBy(User cargoVerifiedBy) {
		this.cargoVerifiedBy = cargoVerifiedBy;
	}
	
	public Date getDateCargoVerified() {
		return dateCargoVerified;
	}
	public void setDateCargoVerified(Date dateCargoVerified) {
		this.dateCargoVerified = dateCargoVerified;
	}
	
	public User getExpensesVerifiedBy() {
		return expensesVerifiedBy;
	}
	public void setExpensesVerifiedBy(User expensesVerifiedBy) {
		this.expensesVerifiedBy = expensesVerifiedBy;
	}
	public Date getDateExpensesVerified() {
		return dateExpensesVerified;
	}
	public void setDateExpensesVerified(Date dateExpensesVerified) {
		this.dateExpensesVerified = dateExpensesVerified;
	}
	
	public User getVerifiedBy() {
		return verifiedBy;
	}
	public void setVerifiedBy(User verifiedBy) {
		this.verifiedBy = verifiedBy;
	}
	public Date getDateVerified() {
		return dateVerified;
	}
	public void setDateVerified(Date dateVerified) {
		this.dateVerified = dateVerified;
	}
	
	public OrderStatus getStatus() {
		return status;
	}
	public void setStatus(OrderStatus status) {
		this.status = status;
	}
	public double getFinalCargoWeight() {
		return finalCargoWeight;
	}
	public void setFinalCargoWeight(double finalCargoWeight) {
		this.finalCargoWeight = finalCargoWeight;
	}
	public double getOdometerStart() {
		return odometerStart;
	}
	public void setOdometerStart(double odometerStart) {
		this.odometerStart = odometerStart;
	}
	public double getOdometerEnd() {
		return odometerEnd;
	}
	public void setOdometerEnd(double odometerEnd) {
		this.odometerEnd = odometerEnd;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public List<Toll> getTolls() {
		return tolls;
	}
	public void setTolls(List<Toll> tolls) {
		this.tolls = tolls;
	}
	public double getFuelUsed() {
		return fuelUsed;
	}
	public void setFuelUsed(double fuelUsed) {
		this.fuelUsed = fuelUsed;
	}
	
}
