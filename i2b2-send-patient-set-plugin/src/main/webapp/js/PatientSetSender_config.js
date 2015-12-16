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
		var userIdVar = username ? username.substring(username.indexOf('#') + 1) : username;
		var cvrgPatientSet = {
				queryName: i2b2.PatientSetSender.model.prsRecord.sdxInfo.sdxDisplayName.substring(0, 50),
				userId: userIdVar,
				subjects: patientSet.patients
			};
		return {
			json: Object.toJSON(cvrgPatientSet)
		};
	}
};

i2b2.PatientSetSender.contextualize = i2b2.PatientSetSender.context.DEFAULT;
