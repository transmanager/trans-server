package channy.transmanager.shaobao.model.user;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import channy.transmanager.shaobao.feature.Action;
import channy.transmanager.shaobao.feature.Module;
import channy.transmanager.shaobao.feature.Page;
import channy.transmanager.shaobao.model.BaseEntity;
import channy.transmanager.shaobao.model.Department;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@Table(name = "users" )
public class User extends BaseEntity {
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 8402718959158601647L;
	private String name;
	private String password;
	private boolean isLocked = false;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade(value = {CascadeType.SAVE_UPDATE})
	@JoinColumn(name = "department")
	private Department department;
	
	//@Transient
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role")
	@Cascade(CascadeType.SAVE_UPDATE)
	private Role role = null;
	
	@Column(unique = true)
	private String employeeId;
	
	@Enumerated(EnumType.STRING)
	private UserStatus status = UserStatus.Normal;
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isLocked() {
		return isLocked;
	}

	public void setLocked(boolean isLocked) {
		this.isLocked = isLocked;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
	
	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	public boolean isGranted(Module module) {
		if (role == null) {
			return false;
		}
		
		return role.getGrantedModules().contains(module);
	}
	
	public boolean isGranted(Page page) {
		if (role == null) {
			return false;
		}
		
		return role.getGrantedPages().contains(page);
	}
	
	public boolean isGranted(Action action) {
		if (role == null) {
			return false;
		}
		
		return role.getGrantedActions().contains(action);
	}
	
	public Set<Module> getGrantedModules() {
		if (role == null) {
			return new HashSet<Module>();
		}
		
		return role.getGrantedModules();
	}
	
	public Set<Page> getGrantedPages() {
		if (role == null) {
			return new HashSet<Page>();
		}
		
		return role.getGrantedPages();
	}
	
	public Set<Action> getGrantedActions() {
		if (role == null) {
			return new HashSet<Action>();
		}
		
		return role.getGrantedActions();
	}
}
