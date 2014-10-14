package channy.transmanager.shaobao.model.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "repairmen")
public class Repairman extends User {

	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 974724045996024068L;

}
