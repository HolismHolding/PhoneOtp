<#-- Phone form template for Keycloak Provider -->

<#-- Translations / text labels -->
<#assign phoneLabel = "Phone Number">
<#assign sendOtpText = "Send OTP">
<#assign changePhoneText = "Change Phone">
<#assign emptyPhoneText = "Phone number cannot be empty">
<#assign invalidPhoneText = "Invalid phone number">

<form id="phone-form" action="${url.loginAction}" method="POST">
    <div class="phone-form">
        <h2>${phoneLabel}</h2>

        <#-- Phone Input -->
        <div class="${properties.kcFormGroupClass!}">
            <label for="phone" class="${properties.kcLabelClass!}">${phoneLabel}</label>
            <input tabindex="1" id="phone" class="${properties.kcInputClass!}" name="phone" value="${phone!''}" type="tel" 
                   placeholder="${phoneLabel}" aria-invalid="<#if emptyPhone??>true</#if>"
                   />

            <#-- Display error messages -->
            <#if errorMessage?? && errorMessage?has_content>
                <span class="${properties.kcInputErrorMessageClass!}" aria-live="polite">${errorMessage}</span>
            </#if>
            <#if emptyPhone?? && emptyPhone>
                <span class="${properties.kcInputErrorMessageClass!}" aria-live="polite">${emptyPhoneText}</span>
            </#if>
            <#if invalidPhone?? && invalidPhone>
                <span class="${properties.kcInputErrorMessageClass!}" aria-live="polite">${invalidPhoneText}</span>
            </#if>
        </div>

        <#-- Send OTP Button -->
        <div class="${properties.kcFormGroupClass!}">
            <button type="submit" class="btn-send-otp ${properties.kcButtonClass!} ${properties.kcButtonPrimaryClass!}"
                    id="submit-btn">
                ${sendOtpText}
            </button>
        </div>

        <#-- Change Phone Option -->
        <#if visibleOtp?? && (phone?? && phone?has_content)>
            <div class="kc-form-options-wrapper">
                <button class="btn-change-phone" onclick="changePhoneNumber()">
                    ${changePhoneText}
                </button>
            </div>
        </#if>
    </div>
</form>

<script>
    // Disable the submit button once clicked
    document.getElementById('phone-form').onsubmit = function() {
        document.getElementById('submit-btn').disabled = true;
        document.getElementById('submit-btn').innerText = 'Sending OTP...'; // Optional: change button text while submitting
        return true; // Let the form submit normally
    }

    function changePhoneNumber() {
        // Logic to trigger phone number change (may need further implementation)
    }
</script>
