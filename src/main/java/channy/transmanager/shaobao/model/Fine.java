package channy.transmanager.shaobao.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "fines")
public class Fine extends Expenses {
	public Fine() {
		super.setType("罚单");
	}
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3648427875204414029L;
	
}
