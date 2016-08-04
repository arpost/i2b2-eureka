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

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * A bean class to hold information related to i2b2 projects in the system.
 *
 * @author hrathod
 *
 */
@Entity
@Table(name = "i2b2projects")
public class I2b2ProjectEntity {

	/**
	 * The project's unique identifier.
	 */
	@Id
	@SequenceGenerator(name = "I2B2PRJ_SEQ_GENERATOR", sequenceName = "I2B2PRJ_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "I2B2PRJ_SEQ_GENERATOR")
	private Long id;
	/**
	 * The project's name.
	 */
	@Column(unique = true, nullable = false)
	private String name;

	@JoinColumn(nullable = false)
	@ManyToOne
	private I2b2DomainEntity i2b2Domain;

	/**
	 * Get the project's identification number.
	 *
	 * @return A {@link Long} representing the project's id.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the project's identification number.
	 *
	 * @param inId The number representing the project's id.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * Get the project's name.
	 *
	 * @return A String containing the project's name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the project's name.
	 *
	 * @param inName A string containing the project's name.
	 */
	public void setName(String inName) {
		this.name = inName;
	}

	public I2b2DomainEntity getI2b2Domain() {
		return i2b2Domain;
	}

	public void setI2b2Domain(I2b2DomainEntity i2b2Domain) {
		this.i2b2Domain = i2b2Domain;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 83 * hash + Objects.hashCode(this.id);
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
		final I2b2ProjectEntity other = (I2b2ProjectEntity) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "I2b2ProjectEntity{" + "id=" + id + ", name=" + name + ", i2b2Domain=" + i2b2Domain + '}';
	}

}
