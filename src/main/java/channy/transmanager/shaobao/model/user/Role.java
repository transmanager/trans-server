package channy.transmanager.shaobao.model.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;

import channy.transmanager.shaobao.feature.Action;
import channy.transmanager.shaobao.feature.Module;
import channy.transmanager.shaobao.feature.Page;
import channy.transmanager.shaobao.model.BaseEntity;

@Entity
@Table(name="roles")
public class Role extends BaseEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6571661773423557876L;

	private String name;

	@ElementCollection(targetClass = Module.class, fetch = FetchType.EAGER)
	private Set<Module> grantedModules = new HashSet<Module>();

	@ElementCollection(targetClass = Page.class, fetch = FetchType.EAGER)
	private Set<Page> grantedPages = new HashSet<Page>();

	@ElementCollection(targetClass = Action.class, fetch = FetchType.EAGER)
	private Set<Action> grantedActions = new HashSet<Action>();

	private boolean editable = false;

	public Set<Module> getGrantedModules() {
		return grantedModules;
	}

	public void setGrantedModules(Set<Module> grantedModules) {
		this.grantedModules = grantedModules;
	}

	public Set<Page> getGrantedPages() {
		return grantedPages;
	}

	public void setGrantedPages(Set<Page> grantedPages) {
		this.grantedPages = grantedPages;
	}

	public Set<Action> getGrantedActions() {
		return grantedActions;
	}

	public void setGrantedActions(Set<Action> grantedActions) {
		this.grantedActions = grantedActions;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean isEditable) {
		this.editable = isEditable;
	}
}
