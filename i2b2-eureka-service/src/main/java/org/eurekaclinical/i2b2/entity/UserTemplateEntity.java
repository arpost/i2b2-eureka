package org.eurekaclinical.i2b2.entity;

/*-
 * #%L
 * i2b2 Eureka Service
 * %%
 * Copyright (C) 2015 - 2016 Emory University
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author ANdrew Post
 */
@Table(name = "usertemplates")
@Entity
public class UserTemplateEntity implements org.eurekaclinical.standardapis.entity.UserTemplateEntity<RoleEntity> {

	/**
	 * The user's unique identifier.
	 */
	@Id
	@SequenceGenerator(name = "UT_SEQ_GENERATOR", sequenceName = "UT_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "UT_SEQ_GENERATOR")
	private Long id;

	/**
	 * The user's email address.
	 */
	@Column(unique = true, nullable = false)
	private String name;
	
	private boolean autoAuthorize;

	/**
	 * A list of roles assigned to the user.
	 */
	@ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
	@JoinTable(name = "usertemplate_role",
			joinColumns = {
				@JoinColumn(name = "usertemplate_id")},
			inverseJoinColumns = {
				@JoinColumn(name = "role_id")})
	private List<RoleEntity> roles = new ArrayList<>();

	/**
	 * A list of roles assigned to the user.
	 */
	@ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
	@JoinTable(name = "usertemplate_group",
			joinColumns = {
				@JoinColumn(name = "usertemplate_id")},
			inverseJoinColumns = {
				@JoinColumn(name = "group_id")})
	private List<GroupEntity> groups = new ArrayList<>();

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public void setId(Long inId) {
		this.id = inId;
	}

	@Override
	public void setName(String inName) {
		this.name = inName;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public boolean isAutoAuthorize() {
		return autoAuthorize;
	}

	public void setAutoAuthorize(boolean autoAuthorize) {
		this.autoAuthorize = autoAuthorize;
	}

	@Override
	public List<RoleEntity> getRoles() {
		return new ArrayList<>(this.roles);
	}

	@Override
	public void setRoles(List<RoleEntity> inRoles) {
		if (inRoles == null) {
			this.roles = new ArrayList<>();
		} else {
			this.roles = new ArrayList<>(inRoles);
		}
	}
	
	@Override
	public void addRole(RoleEntity role) {
		if (!this.roles.contains(role)) {
			this.roles.add(role);
		}
	}
	
	@Override
	public void removeRole(RoleEntity role) {
		this.roles.remove(role);
	}
	
	public List<GroupEntity> getGroups() {
		return new ArrayList<>(groups);
	}

	public void setGroups(List<GroupEntity> inGroups) {
		if (inGroups == null) {
			this.groups = new ArrayList<>();
		} else {
			this.groups = new ArrayList<>(inGroups);
		}
	}
	
	public void addGroup(GroupEntity group) {
		if (!this.groups.contains(group)) {
			this.groups.add(group);
		}
	}
	
	public void removeGroup(GroupEntity group) {
		this.groups.remove(group);
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 79 * hash + Objects.hashCode(this.id);
		return hash;
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
		final UserTemplateEntity other = (UserTemplateEntity) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "UserTemplateEntity{" + "id=" + id + ", name=" + name + ", autoAuthorize=" + autoAuthorize + ", roles=" + roles + ", groups=" + groups + '}';
	}
	
}
