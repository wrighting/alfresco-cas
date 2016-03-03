
var userMenuWidgets = 
  widgetUtils.findObject(model.jsonModel, "id", "HEADER_USER_MENU");
if (userMenuWidgets != null)
{
    //Would need to replace the LogoutService module as well
    //Also logout is now a POST which makes life more complicated
    userMenuWidgets.config.widgets.push({
        id: "HEADER_USER_MENU_LOGOUT",
        name: "alfresco/header/AlfMenuItem",
        config:
        {
           id: "HEADER_USER_MENU_LOGOUT",
           label: "logout.label",
           iconClass: "alf-user-logout-icon",
           publishTopic: "ALF_DOLOGOUT",
        }
     });

}

//Replace the alfresco LogoutService with our version
//services can be a string or an object
var myservices = model.jsonModel.services;
for(var i = 0, len = myservices.length; i < len; i++) {
    if (typeof myservices[i] === "string" && myservices[i] == "alfresco/services/LogoutService") {
    	model.jsonModel.services[i] = "Authentication/LogoutService";
    }
}

widgetUtils.deleteObjectFromArray(model.jsonModel, "id", "HEADER_USER_MENU_PASSWORD"); 
/* Alternative replace the URL of the change password link
var changePasswordItem = 
	  widgetUtils.findObject(model.jsonModel, "id", "HEADER_USER_MENU_PASSWORD");
if (changePasswordItem != null)
{
	changePasswordItem.config.targetUrl = "change password link";
}
*/
