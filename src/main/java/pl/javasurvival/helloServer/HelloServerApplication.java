package pl.javasurvival.helloServer;

import io.vavr.collection.List;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class HelloServerApplication {
    private List<Message> messages = List.empty();

    private HelloServerApplication() {
        messages = messages.append( new Message("test content", "Zenek Testowy"));
        messages = messages.append( new Message("bla bla", "Sebastian Tester"));
    }

    public static void main(String[] args) {
        new HelloServerApplication().serve();

    }

    private void serve() {
        RouterFunction route = nest( path("/api"),
                route(GET("/time"), renderTime())
                .andRoute(GET("/messages"), renderMessages())
                .andRoute(POST("/messages"), postMessage()));

        HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);
        HttpServer server = HttpServer.create("localhost", 8080);
        ReactorHttpHandlerAdapter myReactorHandler = new ReactorHttpHandlerAdapter(httpHandler);
        server.startAndAwait(myReactorHandler);
    }

    private HandlerFunction<ServerResponse> postMessage() {
        return request -> {
            Mono<Message> postedMessage = request.bodyToMono(Message.class);
            return postedMessage.flatMap( message -> {
                addMessage(message);
                return ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(fromObject(messages.toJavaList()));
            });

        };
    }

    private HandlerFunction<ServerResponse> renderMessages() {
        return request -> {
            return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromObject(messages.toJavaList()));
        };
    }

    private HandlerFunction<ServerResponse> renderTime() {
        return request -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            return ServerResponse.ok()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(fromObject(myFormatter.format(now)));
        };
    }


    private synchronized void addMessage( Message newMessage) {
        messages = messages.append(newMessage);
    }
}
