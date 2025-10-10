<#-- OTP form template for Keycloak Provider -->

<#-- Translate messages from Keycloak localization -->
<#assign otpLabel = "Enter OTP">
<#assign signInText = "Sign In">
<#assign emptyOtpText = "OTP cannot be empty">
<#assign invalidOtpText = "Invalid OTP">
<#assign timerText = "Resend OTP in">
<#assign resendOtpText = "Resend OTP">
<#assign signingInText = "Signing In...">
<#assign sendingOtpText = "Sending OTP...">

<div class="otp-form">
    <h2>${otpLabel}</h2>

    <#-- OTP Input -->
    <div class="input-container">
        <label for="otp">${otpLabel}</label>
        <input type="text" id="otp" name="otp" placeholder="${otpLabel}" class="input-field" value="${otp}" />
        
        <#-- Display error messages -->
        <#if emptyOtp??>
            <div class="error-message">${emptyOtpText}</div>
        </#if>
        <#if invalidOtp??>
            <div class="error-message">${invalidOtpText}</div>
        </#if>
    </div>

    <#-- Submit OTP Button -->
    <div class="submit-container">
        <button type="submit" class="btn-submit-otp" ${signingIn ? 'disabled' : ''}>${signingIn ? signingInText : signInText}</button>
    </div>

    <#-- Timer for Resend OTP -->
    <#if timer?? && timer > 0>
        <div class="timer-container">${timerText} ${timer} seconds</div>
    <#elseif sendingOtp?? && sendingOtp == true>
        <div class="sending-container">Sending OTP...</div>
    <#else>
        <a href="#" class="resend-otp-link" onclick="resendOtp()">${resendOtpText}</a>
    </#if>
</div>

<script>
    function resendOtp() {
        // Logic to trigger OTP resend
    }
</script>
