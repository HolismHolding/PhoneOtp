<#-- OTP form template for Keycloak Provider (i18n-enabled) -->

<form id="otp-form" action="${url.loginAction}" method="POST">
    <div class="otp-form">
        <h2>${msg("otpLabel")}</h2>

        <div class="${properties.kcFormGroupClass!}">
            <label for="otp" class="${properties.kcLabelClass!}">${msg("otpLabel")}</label>
            <input tabindex="1" id="otp" class="${properties.kcInputClass!}" name="otp" type="text"
                   value="${otp!''}" placeholder="${msg("otpLabel")}" aria-invalid="<#if otpErrorKey??>true</#if>" />

            <#-- Display error messages based on keys set in Auth.java -->
            <#if otpErrorKey??>
                <span class="${properties.kcInputErrorMessageClass!}" aria-live="polite">
                    ${msg(otpErrorKey)}
                </span>
            </#if>
        </div>

        <div class="${properties.kcFormGroupClass!}">
            <button type="submit"
                    class="btn-submit-otp ${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!}"
                    id="submit-btn">
                ${msg("signInText")}
            </button>
        </div>

        <#-- Timer and resend logic -->
        <#if timer?? && timer > 0>
            <div class="timer-container">${msg("timerText")} ${timer} ${msg("secondsText")}</div>
        <#elseif sendingOtp?? && sendingOtp == true>
            <div class="sending-container">${msg("sendingOtpText")}</div>
        <#else>
            <a href="#" class="resend-otp-link" onclick="resendOtp()">${msg("resendOtpText")}</a>
        </#if>
    </div>
</form>

<script>
    document.getElementById('otp-form').onsubmit = function() {
        const btn = document.getElementById('submit-btn')
        btn.disabled = true
        btn.innerText = '${msg("signingInText")}'
        return true
    }

    function resendOtp() {}
</script>
