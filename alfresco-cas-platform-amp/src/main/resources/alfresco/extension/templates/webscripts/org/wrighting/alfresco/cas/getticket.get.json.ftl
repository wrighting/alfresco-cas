<#escape x as jsonUtils.encodeJSONString(x)>
{
   <#if alf_token??>
   "ticket": "${alf_token}"
   </#if>
}
</#escape>
