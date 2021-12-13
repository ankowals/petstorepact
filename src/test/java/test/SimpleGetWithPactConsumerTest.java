package test;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import generated.client.api.PetApi;
import generated.client.invoker.ApiClient;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ResponseOptions;
import mock.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static generated.client.invoker.GsonObjectMapper.gson;
import static generated.client.model.Pet.StatusEnum.SOLD;
import static io.restassured.RestAssured.config;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "PetStoreProvider", hostInterface = "localhost", port = "9090")
public class SimpleGetWithPactConsumerTest {

    private PetApi api;

    @Pact(provider="PetStoreProvider", consumer="PetStoreConsumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        return builder
                .uponReceiving("FindByStatus test interaction")
                .path("/pet/findByStatus")
                .query("status=sold")
                .method("GET")
                .willRespondWith()
                .status(200)
                .body(Mock.getFindBySoldStatusBody())
                .toPact();
    }

    @BeforeEach
    void createApi(MockServer mockServer) {
        api = ApiClient.api(ApiClient.Config.apiConfig().reqSpecSupplier(
                () -> new RequestSpecBuilder()
                        .setConfig(config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(gson())))
                        .addFilter(new RequestLoggingFilter())
                        .addFilter(new ResponseLoggingFilter())
                        .setBaseUri(mockServer.getUrl()))).pet();
    }

    @Test
    void shouldFindSoldPets() {
        int statusCode = api.findPetsByStatus()
                .statusQuery(SOLD.getValue())
                .execute(ResponseOptions::andReturn)
                .statusCode();

        assertThat(statusCode).isEqualTo(200);
    }
}
