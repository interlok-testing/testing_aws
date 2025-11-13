package com.adaptris.elastic.test;

import com.adaptris.testing.DockerComposeFunctionalTest;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.containers.wait.strategy.WaitStrategy;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;

import java.io.File;
import java.net.InetSocketAddress;
import java.net.URI;
import java.time.Duration;

import static io.restassured.RestAssured.*;

public class DefaultFunctionalTest extends DockerComposeFunctionalTest {
    protected static String INTERLOK_SERVICE_NAME = "interlok-1";
    protected static String LOCALSTACK_SERVICE_NAME = "localstack-1";
    protected static int INTERLOK_PORT = 8081;
    protected static int LOCALSTACK_PORT = 4566;
    protected static WaitStrategy defaultWaitStrategy = Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(90));

    protected ComposeContainer setupContainers() {
        return new ComposeContainer(new File("docker-compose.yaml"))
                .withExposedService(INTERLOK_SERVICE_NAME, INTERLOK_PORT, defaultWaitStrategy)
                .withExposedService(LOCALSTACK_SERVICE_NAME, LOCALSTACK_PORT, defaultWaitStrategy);
    }

    protected String getKinesisEndpoint(String path) {
        InetSocketAddress address = getHostAddressForService(LOCALSTACK_SERVICE_NAME, LOCALSTACK_PORT);
        if (!path.startsWith("/")) path = "/" + path;
        return "http://" + address.getHostString() + ":" + address.getPort() + path;
    }

    protected String getInterlokApiEndpoint(String path) {
        InetSocketAddress address = getHostAddressForService(INTERLOK_SERVICE_NAME, INTERLOK_PORT);
        if (!path.startsWith("/")) path = "/" + path;
        return "http://" + address.getHostString() + ":" + address.getPort() + path;}



    @Test
    public void test_kinesis_split() throws Exception{
        // index json document
        given().body("""
            <messages>
                <message>this</message>
                <message>is</message>
                <message>a</message>
                <message>sentence</message>
            </messages>""")
        .when().post(getInterlokApiEndpoint("/aws2/kinesis?stream=stream1&partition=partition1"))
                .then().statusCode(200);
    }


}
