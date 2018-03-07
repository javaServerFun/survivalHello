package pl.javasurvival.helloServer;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBoardApplicationTest {
    @Test
    public void createdTopicHasCorrectName() {
        MessageBoardApplication app = new MessageBoardApplication();
        WebTestClient testServer = WebTestClient
                .bindToRouterFunction(app.prepareRouterFunction())
                .build();

        testServer
                .post()
                .uri("/api/messages/java")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(Mono.just( new Message("test1", "testowy")), Message.class)
                .exchange()
                .expectStatus()
                .isOk();


        testServer
                .get()
                .uri("/api/messages/java")
                .exchange()
                .expectBody()
                .jsonPath("$[0].content")
                .isEqualTo("test1");

    }
}
