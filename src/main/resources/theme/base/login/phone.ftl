<#-- Phone form template for Keycloak Provider (i18n-enabled) -->

<form id="phone-form" action="${url.loginAction}" method="POST">
    <div class="phone-form">
        <h2>${msg("phoneLabel")}</h2>

        <div class="${properties.kcFormGroupClass!}">
            <label for="phone" class="${properties.kcLabelClass!}">${msg("phoneLabel")}</label>
            <input tabindex="1" id="phone" class="${properties.kcInputClass!}" name="phone" value="${phone!''}" type="tel" 
                   placeholder="${msg("phoneLabel")}" aria-invalid="<#if phoneErrorKey??>true</#if>" />

            <#-- Display error messages based on keys set in Auth.java -->
            <#if phoneErrorKey??>
                <span class="${properties.kcInputErrorMessageClass!}" aria-live="polite">
                    ${msg(phoneErrorKey)}
                </span>
            </#if>
        </div>

        <div class="${properties.kcFormGroupClass!}">
            <button type="submit" class="btn-send-otp ${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!}"
                    id="submit-btn">
                ${msg("sendOtpText")}
            </button>
        </div>

        <#if visibleOtp?? && (phone?? && phone?has_content)>
            <div class="kc-form-options-wrapper">
                <button class="btn-change-phone" onclick="changePhoneNumber()">
                    ${msg("changePhoneText")}
                </button>
            </div>
        </#if>
    </div>
</form>

<script>
    document.getElementById('phone-form').onsubmit = function() {
        const btn = document.getElementById('submit-btn')
        btn.disabled = true
        btn.innerText = '${msg("sendingOtpText")}'
        return true
    }

    function changePhoneNumber() {}
</script>
