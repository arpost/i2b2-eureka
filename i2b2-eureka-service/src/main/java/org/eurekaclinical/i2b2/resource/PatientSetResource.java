package org.eurekaclinical.i2b2.resource;

/*
 * #%L
 * i2b2 Eureka Service
 * %%
 * Copyright (C) 2015 Emory University
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
import org.eurekaclinical.i2b2.props.I2b2EurekaServicesProperties;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.eurekaclinical.common.comm.clients.ClientException;
import org.eurekaclinical.eureka.client.EurekaClient;
import org.eurekaclinical.eureka.client.comm.DefaultSourceConfigOption;
import org.eurekaclinical.eureka.client.comm.Destination;
import org.eurekaclinical.eureka.client.comm.Job;
import org.eurekaclinical.eureka.client.comm.JobSpec;
import org.eurekaclinical.eureka.client.comm.JobStatus;
import org.eurekaclinical.eureka.client.comm.PatientSetExtractorDestination;
import org.eurekaclinical.eureka.client.comm.SourceConfig;
import org.eurekaclinical.eureka.client.comm.SourceConfig.Section;
import org.eurekaclinical.eureka.client.comm.SourceConfigOption;
import org.eurekaclinical.standardapis.exception.HttpStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service for i2b2 to request that a specified patient set be sent to another
 * service.
 *
 * @author Andrew Post
 */
@Path("/protected/patientset")
@Produces(MediaType.APPLICATION_JSON)
public class PatientSetResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(PatientSetResource.class);

	private final EurekaClient eurekaClient;
	private final I2b2EurekaServicesProperties properties;

	@Inject
	public PatientSetResource(EurekaClient inEurekaClient, I2b2EurekaServicesProperties inProperties) {
		this.eurekaClient = inEurekaClient;
		this.properties = inProperties;
	}

	@GET
	public Response doSend(
			@QueryParam("resultInstanceId") String resultInstanceId, 
			@QueryParam("action") String actionId) 
			throws ClientException {
		InputStream inputStream = null;
		try {
			JobSpec jobSpec = new JobSpec();
			jobSpec.setUpdateData(false);
			jobSpec.setDestinationId(actionId);
			jobSpec.setSourceConfigId(properties.getSourceConfigId());
			SourceConfig sc = new SourceConfig();
			sc.setId(properties.getSourceConfigId());
			Section section = new Section();
			section.setId("edu.emory.cci.aiw.i2b2etl.dsb.I2B2DataSourceBackend");
			DefaultSourceConfigOption option = new DefaultSourceConfigOption();
			option.setName("resultInstanceId");
			option.setValue(new Long(resultInstanceId));
			section.setOptions(new SourceConfigOption[]{option});
			sc.setDataSourceBackends(new Section[]{section});
			jobSpec.setPrompts(sc);
			
			Destination destination = this.eurekaClient.getDestination(actionId);
			if (destination == null || !(destination instanceof PatientSetExtractorDestination)) {
				throw new HttpStatusException(Status.PRECONDITION_FAILED, "Invalid action id " + actionId);
			}
			
			jobSpec.setPropositionIds(Arrays.asList(((PatientSetExtractorDestination) destination).getAliasPropositionId()));
			Long jobId = this.eurekaClient.submitJob(jobSpec);

			Job job;
			JobStatus status;
			do {
				try {
					Thread.sleep(5000L);
				} catch (InterruptedException ex) {
					return Response.status(Status.SERVICE_UNAVAILABLE).build();
				}
				job = this.eurekaClient.getJob(jobId);
				status = job.getStatus();
			} while (status != JobStatus.COMPLETED && status != JobStatus.FAILED);
			inputStream = this.eurekaClient.getOutput(jobSpec.getDestinationId());
			return Response.ok(inputStream).type(MediaType.APPLICATION_JSON).build();
		} catch (ClientException ex) {
			logError(ex);
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ioe) {
					ex.addSuppressed(ioe);
				}
			}
			throw ex;
		}
	}

	private static void logError(Throwable e) {
		LOGGER.error("Exception thrown: {}", e);
	}
}
