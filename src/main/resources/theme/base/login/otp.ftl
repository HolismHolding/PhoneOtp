<#import "template.ftl" as layout>
<@layout.layoutBody>

<form id="otp-form" action="${url.loginAction}" method="POST">
    <div class="otp-form">
        <h2>${msg("otpLabel")}</h2>

        <div class="${properties.kcFormGroupClass!}">
            <label for="otp" class="${properties.kcLabelClass!}">${msg("otpLabel")}</label>
            <input tabindex="1"
                   id="otp"
                   class="${properties.kcInputClass!}"
                   name="otp"
                   type="text"
                   value="${otp!''}"
                   placeholder="${msg("otpLabel")}"
                   aria-invalid="<#if otpErrorKey??>true</#if>" />

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

        <#if timer?? && timer > 0>
            <div class="timer-container">
                ${msg("timerText")} ${timer} ${msg("secondsText")}
            </div>
        <#elseif sendingOtp?? && sendingOtp == true>
            <div class="sending-container">${msg("sendingOtpText")}</div>
        <#else>
            <button type="button" class="resend-otp-link" onclick="resendOtp()">
                ${msg("resendOtpText")}
            </button>
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

</@layout.layoutBody>
