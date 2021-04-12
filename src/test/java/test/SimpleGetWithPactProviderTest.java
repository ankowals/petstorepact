package test;

import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.VerificationReports;
import container.PetStoreContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@Provider("PetStoreProvider")
@PactFolder("build/pacts")
@ExtendWith(PactVerificationInvocationContextProvider.class)
public class SimpleGetWithPactProviderTest {

    @Container
    private static final PetStoreContainer PET_STORE_CONTAINER = new PetStoreContainer();

    @BeforeEach
    void before(PactVerificationContext context) {
       context.setTarget(new HttpTestTarget(PET_STORE_CONTAINER.getHost(), PET_STORE_CONTAINER.getMappedPort(8080), "/v2"));
    }

    @TestTemplate
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }
}
