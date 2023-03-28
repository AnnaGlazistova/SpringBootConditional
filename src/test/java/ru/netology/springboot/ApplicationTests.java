package ru.netology.springboot;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.GenericContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

    @Autowired
    TestRestTemplate restTemplate;

    private static final GenericContainer<?> myDevApp = new GenericContainer<>("devapp").withExposedPorts(8080);
    private static final GenericContainer<?> myProdApp = new GenericContainer<>("proapp").withExposedPorts(8081);

    @BeforeAll
    public static void setUp() {
        myDevApp.start();
        myProdApp.start();
    }

    @Test
    void contextLoadsDevapp() {
        final String expected = "Current profile is dev";
        String url = String.format("http://localhost:%d/profile", myDevApp.getMappedPort(8080));
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        Assertions.assertEquals(expected, forEntity.getBody());
    }

    @Test
    void contextLoadsPpodapp() {
        final String expected = "Current profile is production";
        String url = String.format("http://localhost:%d/profile", myProdApp.getMappedPort(8081));
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
        Assertions.assertEquals(expected, forEntity.getBody());
    }
}
