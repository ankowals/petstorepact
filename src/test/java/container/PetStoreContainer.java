package container;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class PetStoreContainer extends GenericContainer<PetStoreContainer> {

    public PetStoreContainer() {
        super(DockerImageName.parse("swaggerapi/petstore"));
        this.withEnv("SWAGGER_HOST", "localhost")
                .withEnv("SWAGGER_URL", "http://localhost:8080")
                .withEnv("SWAGGER_BASE_PATH", "/v2")
                .withExposedPorts(8080);
    }

    public String getUrl() {
        return "http://" + this.getHost() + ":" + this.getMappedPort(8080) + "/v2";
    }

}
