package channy.transmanager.shaobao.model.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "oilers")
public class Oiler extends User {

	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -5002292443668577824L;

}
