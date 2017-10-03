package com.demo.app.domain.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(schema = "security", name = "user_authorities")
@GenericGenerator(name = "string-seq-generator", strategy = "com.demo.app.configuration.StringSequenceIdentifier", parameters = {
		@org.hibernate.annotations.Parameter(name = "sequence_name", value = "security.authority_seq"),
		@org.hibernate.annotations.Parameter(name = "sequence_prefix", value = "")
})
public class Authority implements Serializable, GrantedAuthority {

	private static final long serialVersionUID = 9171141200000360825L;

	@Id
	@GeneratedValue(generator = "string-seq-generator")
	private String id;

	@ManyToOne
	@JoinColumn(name = "username", nullable = false)
	@NotNull
	private User username;

	@Enumerated(EnumType.STRING)
	@Column(name = "AUTHORITY")
	@NotNull
	private Rol authority;

	public Authority() {
		super();
	}

	public Authority(Rol authority) {
		super();
		this.authority = authority;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUsername() {
		return username;
	}

	public void setUsername(User username) {
		this.username = username;
	}

	@Override
	public String getAuthority() {
		return authority.toString();
	}

	public void setAuthority(String authority) {
		this.authority = Rol.valueOf(authority);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (authority == null ? 0 : authority.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Authority other = (Authority) obj;
		if (authority != other.authority) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Authority [username=" + username + ", authority=" + authority.toString() + "]";
	}

}
