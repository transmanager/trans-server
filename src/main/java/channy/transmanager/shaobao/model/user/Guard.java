package channy.transmanager.shaobao.model.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "guards")
public class Guard extends User {

	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -6921028730958889948L;

}
