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
import javax.persistence.Table;

/**
 * A bean class to hold information related to roles in the system.
 *
 * @author hrathod
 *
 */
@Entity
@Table(name = "i2b2domains")
public class I2b2DomainEntity implements org.eurekaclinical.standardapis.entity.Entity {

	/**
	 * The role's unique identifier.
	 */
	@Id
	@SequenceGenerator(name = "I2B2DOMAIN_SEQ_GENERATOR", sequenceName = "I2B2DOMAIN_SEQ",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,
			generator = "I2B2DOMAIN_SEQ_GENERATOR")
	private Long id;
	
	/**
	 * The domain's name.
	 */
	@Column(unique = true, nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String proxyUrl;
	
	@Column(nullable = false)
	private String redirectHost;
	
	@Column(nullable = false)
	private String adminUsername;
	
	@Column(nullable = false)
	private String adminPassword;

	/**
	 * Create an empty role.
	 */
	public I2b2DomainEntity() {
	}

	/**
	 * Get the role's identification number.
	 *
	 * @return A {@link Long} representing the role's id.
	 */
	public Long getId() {
		return this.id;
	}

	/**
	 * Set the role's identification number.
	 *
	 * @param inId The number representing the role's id.
	 */
	public void setId(Long inId) {
		this.id = inId;
	}

	/**
	 * Get the role's name.
	 *
	 * @return A String containing the role's name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Set the role's name.
	 *
	 * @param inName A string containing the role's name.
	 */
	public void setName(String inName) {
		this.name = inName;
	}

	public String getAdminUsername() {
		return adminUsername;
	}

	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}

	public String getAdminPassword() {
		return adminPassword;
	}

	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	public String getProxyUrl() {
		return proxyUrl;
	}

	public void setProxyUrl(String proxyUrl) {
		this.proxyUrl = proxyUrl;
	}

	public String getRedirectHost() {
		return redirectHost;
	}

	public void setRedirectHost(String redirectHost) {
		this.redirectHost = redirectHost;
	}
	
	@Override
	public int hashCode() {
		int hash = 7;
		hash = 37 * hash + Objects.hashCode(this.id);
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
		final I2b2DomainEntity other = (I2b2DomainEntity) obj;
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "I2b2DomainEntity{" + "id=" + id + ", name=" + name + ", proxyUrl=" + proxyUrl + '}';
	}

}
