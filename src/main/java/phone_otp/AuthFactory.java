package phone_otp;

import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import java.util.Collections; 
import java.util.List;

// Corrected import path for Scope is generally not needed if we use the FQN in the method body,
// but let's try the common workaround for the init method.

public class AuthFactory implements AuthenticatorFactory {

    public static final String PROVIDER_ID = "phone-otp-authenticator";

    private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = {
            AuthenticationExecutionModel.Requirement.REQUIRED,
            AuthenticationExecutionModel.Requirement.ALTERNATIVE,
            AuthenticationExecutionModel.Requirement.DISABLED
    };

    @Override
    public Authenticator create(KeycloakSession session) {
        return new Auth();
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public String getDisplayType() {
        return "Phone OTP (Quick)";
    }

    @Override
    public String getHelpText() {
        return "Minimal custom authenticator for phone OTP flow.";
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    // --- FIX APPLIED HERE: Using the fully qualified name for the parameter type ---
    @Override
    public void init(org.keycloak.provider.Config.Scope config) {
        // Now uses the fully qualified name: org.keycloak.provider.Config.Scope
        // This is often the actual location in modern Keycloak versions.
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // No-op
    }

    @Override
    public void close() {
        // No-op
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }
    
    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return Collections.emptyList();
    }
}
