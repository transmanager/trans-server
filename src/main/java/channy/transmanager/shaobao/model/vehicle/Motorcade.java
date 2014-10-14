package channy.transmanager.shaobao.model.vehicle;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import channy.transmanager.shaobao.model.BaseEntity;

@Entity
@Table(name = "motorcades")
public class Motorcade extends BaseEntity {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 6839923710816694491L;
	private String name;
	
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "motorcade")
	@Cascade(value = {CascadeType.REFRESH})
	private Set<Truck> trucks;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Truck> getTrucks() {
		return trucks;
	}

	public void setTrucks(Set<Truck> trucks) {
		this.trucks = trucks;
	}
	
}
