i2b2.PatientSetSender.SERVICE_URL = 'https://ubuntu/i2b2-eureka-service';
i2b2.PatientSetSender.EUREKA_SERVICES_URL = 'https://ubuntu/eureka-services';
i2b2.PatientSetSender.RECEIVER_DISPLAY_NAME = 'Receiver';
i2b2.PatientSetSender.RECEIVER_SEND_URL = 'http://localhost';

i2b2.PatientSetSender.context = {
	DEFAULT: function (patientSet) {
		return {
			postBody: JSON.stringify(patientSet)
		};
	},
	CVRG: function (patientSet) {
		var username = patientSet.username;
		var cvrgPatientSet = {
				queryName: i2b2.PatientSetSender.model.prsRecord.sdxInfo.sdxDisplayName,
				userId: username ? username.substring(username.indexOf('#') + 1) : username,
				subjects: patientSet.patientIds
			};
		return {
			json: JSON.stringify(cvrgPatientSet)
		};
	}
};

i2b2.PatientSetSender.contextualize = i2b2.PatientSetSender.context.DEFAULT;
