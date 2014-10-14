package channy.transmanager.shaobao.model.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "clerks")
public class Clerk extends User {

	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -226456406961822873L;

}
