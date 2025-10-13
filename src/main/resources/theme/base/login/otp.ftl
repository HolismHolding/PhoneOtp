<#-- OTP form template for Keycloak Provider -->

<#-- Translate messages / labels -->
<#assign otpLabel = "Enter OTP">
<#assign signInText = "Sign In">
<#assign emptyOtpText = "OTP cannot be empty">
<#assign invalidOtpText = "Invalid OTP">
<#assign timerText = "Resend OTP in">
<#assign resendOtpText = "Resend OTP">
<#assign signingInText = "Signing In...">
<#assign sendingOtpText = "Sending OTP...">

<form id="otp-form" action="${url.loginAction}" method="POST">
    <div class="otp-form">
        <h2>${otpLabel}</h2>

        <#-- OTP Input -->
        <div class="${properties.kcFormGroupClass!}">
            <label for="otp" class="${properties.kcLabelClass!}">${otpLabel}</label>
            <input tabindex="1" id="otp" class="${properties.kcInputClass!}" name="otp" type="text"
                   value="${otp!''}" placeholder="${otpLabel}" aria-invalid="<#if emptyOtp??>true</#if>" />

            <#-- Display error messages -->
            <#if errorMessage?? && errorMessage?has_content>
                <span class="${properties.kcInputErrorMessageClass!}" aria-live="polite">${errorMessage}</span>
            </#if>
            <#if emptyOtp?? && emptyOtp>
                <span class="${properties.kcInputErrorMessageClass!}" aria-live="polite">${emptyOtpText}</span>
            </#if>
            <#if invalidOtp?? && invalidOtp>
                <span class="${properties.kcInputErrorMessageClass!}" aria-live="polite">${invalidOtpText}</span>
            </#if>
        </div>

        <#-- Submit OTP Button -->
        <div class="${properties.kcFormGroupClass!}">
            <button type="submit"
                    class="btn-submit-otp ${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!}"
                    id="submit-btn">
                ${signInText}
            </button>
        </div>

        <#-- Timer for Resend OTP -->
        <#if timer?? && timer > 0>
            <div class="timer-container">${timerText} ${timer} seconds</div>
        <#elseif sendingOtp?? && sendingOtp == true>
            <div class="sending-container">${sendingOtpText}</div>
        <#else>
            <a href="#" class="resend-otp-link" onclick="resendOtp()">${resendOtpText}</a>
        </#if>
    </div>
</form>

<script>
    // Disable the submit button once clicked
    document.getElementById('otp-form').onsubmit = function() {
        const btn = document.getElementById('submit-btn')
        btn.disabled = true
        btn.innerText = '${signingInText}'
        return true // allow normal submission
    }

    function resendOtp() {
        // Logic to trigger OTP resend (reload or submit hidden form if needed)
    }
</script>
