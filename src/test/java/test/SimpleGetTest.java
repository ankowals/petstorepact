package test;

import org.somecompany.petstorepactclient.api.PetApi;
import org.somecompany.petstorepactclient.invoker.ApiClient;
import org.somecompany.petstorepactclient.model.Pet;
import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import container.PetStoreContainer;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ResponseOptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;

import static org.somecompany.petstorepactclient.invoker.GsonObjectMapper.gson;
import static org.somecompany.petstorepactclient.model.Pet.StatusEnum.SOLD;
import static io.restassured.RestAssured.config;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
public class SimpleGetTest {

    private static PetApi api;
    private static final String SPEC = "/swagger.json";

    @Container
    private static final PetStoreContainer PET_STORE_CONTAINER = new PetStoreContainer();

    @BeforeAll
    static void createApi() {
        api = ApiClient.api(ApiClient.Config.apiConfig().reqSpecSupplier(
                () -> new RequestSpecBuilder()
                        .setConfig(config().objectMapperConfig(objectMapperConfig().defaultObjectMapper(gson())))
                        .addFilter(new RequestLoggingFilter())
                        .addFilter(new ResponseLoggingFilter())
                        //.addFilter(new OpenApiValidationFilter(PET_STORE_CONTAINER.getUrl() + SPEC))
                        .setBaseUri(PET_STORE_CONTAINER.getUrl()))).pet();
    }

    @Test
    public void shouldFindSoldPets() {
        Pet[] pets = api.findPetsByStatus()
                .statusQuery(SOLD.getValue())
                .execute(ResponseOptions::andReturn)
                .as(Pet[].class);

        assertThat(Arrays.asList(pets))
                .extracting(Pet::getStatus)
                .containsOnly(SOLD);

        assertThat(pets.length).isGreaterThan(0);
    }
}
