var userMenuWidgets = widgetUtils.findObject(model.jsonModel, "id",
		"HEADER_USER_MENU");
if (userMenuWidgets != null) {

	var changePasswordItem = widgetUtils.findObject(model.jsonModel, "id",
			"HEADER_USER_MENU_PASSWORD");
	var passwdLink = casProperties.getPasswordLink();

	/*
	 * If property is set, in alfresco-global.properties, replace the URL of the
	 * change password link or add if missing
	 */

	if (changePasswordItem != null) {
		if (passwdLink != null && passwdLink != "") {
			changePasswordItem.config.targetUrl = passwdLink;
			changePasswordItem.config.targetUrlType = "FULL_PATH"; // {@link
																	// module:alfresco/enums/urlTypes#FULL_PATH}
		} else {
			widgetUtils.deleteObjectFromArray(model.jsonModel, "id",
					"HEADER_USER_MENU_PASSWORD");
		}
	} else {
		if (passwdLink != null && passwdLink != "") {
			userMenuWidgets.config.widgets.push({
				id : "HEADER_USER_MENU_PASSWORD",
				name : "alfresco/header/AlfMenuItem",
				config : {
					id : "HEADER_USER_MENU_CHANGE_PASSWORD",
					label : "change_password.label",
					iconClass : "alf-user-password-icon",
					targetUrl : passwdLink,
					targetUrlType : "FULL_PATH" // {@link
												// module:alfresco/enums/urlTypes#FULL_PATH}
				}
			});
		}
	}

	var logoutWidget = widgetUtils.findObject(model.jsonModel, "id",
			"HEADER_USER_MENU_LOGOUT");

	if (logoutWidget == null) {
		userMenuWidgets.config.widgets.push({
			id : "HEADER_USER_MENU_LOGOUT",
			name : "alfresco/header/AlfMenuItem",
			config : {
				id : "HEADER_USER_MENU_LOGOUT",
				label : "logout.label",
				iconClass : "alf-user-logout-icon",
				publishTopic : "ALF_DOLOGOUT",
			}
		});
	}
}

// Replace the alfresco LogoutService with our version
// services can be a string or an object
var myservices = model.jsonModel.services;
for (var i = 0, len = myservices.length; i < len; i++) {
	if (typeof myservices[i] === "string"
			&& myservices[i] == "alfresco/services/LogoutService") {
		model.jsonModel.services[i] = {
			name : "Authentication/LogoutService",
			config : {
				"casLogout" : casProperties.getCasServerLogoutUrl(),
				"paramName" : casProperties.getCasServiceParamName(),
				"logoutDestination" : casProperties.getLogoutDestination()
			}
		};
	}
}
