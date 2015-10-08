/**
 * @module alfresco/services/LogoutService
 * @extends module:alfresco/core/Core
 * @mixes module:alfresco/core/CoreXhr
 * @author Kevin Roast, Ian Wright
 */
define(["dojo/_base/declare",
        "alfresco/core/Core",
        "alfresco/core/CoreXhr",
        "dojo/_base/lang",
        "service/constants/Default"],
        function(declare, AlfCore, CoreXhr, lang, AlfConstants) {

   return declare([AlfCore, CoreXhr], {

      /**
       * Sets up the subscriptions for the LogoutService
       * 
       * @instance
       * @param {array} args Constructor arguments
       */
      constructor: function alfresco_services_LogoutService__constructor(args) {
         lang.mixin(this, args);
         this.alfSubscribe("ALF_DOLOGOUT", lang.hitch(this, "doLogout"));
      },

      /**
       * Perform a logout POST operation.
       * The action of performing this call will return a 401 Unauthorised status code and potentially
       * a 'Location' header for the redirect - this is handled by CoreXhr.
       * 
       * @instance
       */
      doLogout: function alfresco_services_LogoutService__doLogout() {
         this.serviceXhr({
            url: AlfConstants.URL_PAGECONTEXT + "dologout",
            method: "POST",
            data: {
            	redirectURL: "${cas.server.prefix}/logout",
            	redirectURLQueryKey: "service",
            	redirectURLQueryValue: "${cas.logout.dest.url}"	
            },
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            successCallback: this.reloadPage,
            callbackScope: this
         });
         //?
      },

      /**
       * Refresh the current page to ensure logout occurs. This is the callback used by the
       * [doLogout]{@link module:alfresco/services/LogoutService#doLogout} function and will
       * be called on receiving the logout response. Attempting to reload the page will ensure
       * that the user is shown login page having logged out.
       *
       * This should never be called as the CoreXhr handles the 401 response according to the response
       *
       * @instance
       */
      reloadPage: function alfresco_services_LogoutService__refresh() {
         window.location.reload();
      }
   });
});

