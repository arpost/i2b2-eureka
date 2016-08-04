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
 * @author Andrew Post
 */
@Entity
@Table(name = "groups")
public class GroupEntity implements org.eurekaclinical.standardapis.entity.GroupEntity {

	@Id
	@SequenceGenerator(name = "GROUP_SEQ_GENERATOR", sequenceName = "GROUP_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "GROUP_SEQ_GENERATOR")
	private Long id;

	private String name;

	@ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
	@JoinTable(name = "group_i2b2role",
			joinColumns = {
				@JoinColumn(name = "group_id")},
			inverseJoinColumns = {
				@JoinColumn(name = "i2b2role_id")})
	private List<I2b2RoleEntity> i2b2Roles;

	@ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
	@JoinTable(name = "group_i2b2project",
			joinColumns = {
				@JoinColumn(name = "group_id")},
			inverseJoinColumns = {
				@JoinColumn(name = "i2b2project_id")})
	private List<I2b2ProjectEntity> i2b2Projects;

	public GroupEntity() {
		this.i2b2Roles = new ArrayList<>();
		this.i2b2Projects = new ArrayList<>();
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public List<I2b2RoleEntity> getI2b2Roles() {
		return new ArrayList<>(i2b2Roles);
	}

	public void setI2b2Roles(List<I2b2RoleEntity> inI2b2Roles) {
		if (inI2b2Roles == null) {
			this.i2b2Roles = new ArrayList<>();
		} else {
			this.i2b2Roles = new ArrayList<>(inI2b2Roles);
		}
	}

	public void addI2b2Role(I2b2RoleEntity inI2b2Role) {
		if (!this.i2b2Roles.contains(inI2b2Role)) {
			this.i2b2Roles.add(inI2b2Role);
		}
	}

	public void removeI2b2Role(I2b2RoleEntity inI2b2Role) {
		this.i2b2Roles.remove(inI2b2Role);
	}

	public List<I2b2ProjectEntity> getI2b2Projects() {
		return new ArrayList<>(i2b2Projects);
	}

	public void setI2b2Projects(List<I2b2ProjectEntity> inI2b2Projects) {
		if (inI2b2Projects == null) {
			this.i2b2Projects = new ArrayList<>();
		} else {
			this.i2b2Projects = new ArrayList<>(inI2b2Projects);
		}
	}

	public void addI2b2Project(I2b2ProjectEntity inI2b2Project) {
		if (this.i2b2Projects.contains(inI2b2Project)) {
			this.i2b2Projects.add(inI2b2Project);
		}
	}

	public void removeI2b2Project(I2b2ProjectEntity inI2b2Project) {
		this.i2b2Projects.remove(inI2b2Project);
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 73 * hash + Objects.hashCode(this.id);
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
		final GroupEntity other = (GroupEntity) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "GroupEntity{" + "id=" + id + ", name=" + name + ", i2b2Roles=" + i2b2Roles + ", i2b2Projects=" + i2b2Projects + '}';
	}

}
