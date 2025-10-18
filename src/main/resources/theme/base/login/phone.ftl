<#-- Phone form template for Keycloak Provider (i18n-enabled) -->

<form id="phone-form" action="${url.loginAction}" method="POST">
    <div class="phone-form">
        <h2>${msg("phoneLabel")}</h2>

        <div class="${properties.kcFormGroupClass!}">
            <label for="phone" class="${properties.kcLabelClass!}">${msg("phoneLabel")}</label>
            <input tabindex="1" id="phone" class="${properties.kcInputClass!}" name="phone" value="${phone!''}" type="tel" 
                   placeholder="${msg("phoneLabel")}" aria-invalid="<#if emptyPhone??>true</#if>"
                   />

            <#if errorMessage?? && errorMessage?has_content>
                <span class="${properties.kcInputErrorMessageClass!}" aria-live="polite">${errorMessage}</span>
            </#if>
            <#if emptyPhone?? && emptyPhone>
                <span class="${properties.kcInputErrorMessageClass!}" aria-live="polite">${msg("emptyPhoneText")}</span>
            </#if>
            <#if invalidPhone?? && invalidPhone>
                <span class="${properties.kcInputErrorMessageClass!}" aria-live="polite">${msg("invalidPhoneText")}</span>
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
        document.getElementById('submit-btn').disabled = true;
        document.getElementById('submit-btn').innerText = '${msg("sendingOtpText")}';
        return true;
    }

    function changePhoneNumber() {}
</script>
