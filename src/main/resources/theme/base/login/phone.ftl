<#-- Phone form template for Keycloak Provider -->

<#-- Translate messages from Keycloak localization -->
<#assign phoneLabel = "Phone Number">
<#assign sendOtpText = "Send OTP">
<#assign changePhoneText = "Change Phone">
<#assign emptyPhoneText = "Phone number cannot be empty">
<#assign invalidPhoneText = "Invalid phone number">
<#assign sendingOtpText = "Sending OTP...">

<div class="phone-form">
    <h2>${phoneLabel}</h2>
    
    <#-- Phone Input -->
    <div class="input-container">
        <label for="phone">${phoneLabel}</label>
        <input type="tel" id="phone" name="phone" placeholder="${phoneLabel}" class="input-field" value="${phone}" />
        
        <#-- Display error messages -->
        <#if emptyPhone??>
            <div class="error-message">${emptyPhoneText}</div>
        </#if>
        <#if invalidPhone??>
            <div class="error-message">${invalidPhoneText}</div>
        </#if>
    </div>

    <#-- Send OTP Button -->
    <div class="submit-container">
        <button type="submit" class="btn-send-otp"
            <#if sendingOtp?? && sendingOtp>disabled</#if>>
            <#if sendingOtp?? && sendingOtp>${sendingOtpText}<#else>${sendOtpText}</#if>
        </button>
    </div>

    <#-- Change Phone Option -->
    <#if visibleOtp?? && !empty(phone)>
        <button class="btn-change-phone" onclick="changePhoneNumber()">${changePhoneText}</button>
    </#if>
</div>

<script>
    function changePhoneNumber() {
        // Logic to trigger phone number change
    }
</script>
