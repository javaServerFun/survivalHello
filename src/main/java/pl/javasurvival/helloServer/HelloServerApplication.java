package pl.javasurvival.helloServer;

import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.ipc.netty.http.server.HttpServer;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;



import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

public class HelloServerApplication {
    public static void main(String[] args) {
        new HelloServerApplication().serve();

    }

    private void serve() {

        RouterFunction route = nest(path("/api"),
                    route(GET("/time"), renderTime())
                    .andRoute(GET("/messages"), renderMessages())
                    .andRoute(POST("/messages").and(contentType(APPLICATION_JSON)), postMessage()));

        HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);
        HttpServer server = HttpServer.create("localhost", 8080);
        ReactorHttpHandlerAdapter myReactorHandler = new ReactorHttpHandlerAdapter(httpHandler);
        server.startAndAwait(myReactorHandler);
    }

    private HandlerFunction<ServerResponse> postMessage() {
        return request -> {
            final List<Message> messages = new ArrayList<>();

            messages.add(new Message("aaa", "bbb"));
            messages.add(new Message("aaa", "bbb"));
            messages.add(new Message("ccc", "ddde"));
            Mono<Message> message = request.bodyToMono(Message.class);
            final Mono<ServerResponse> res=  message.flatMap( m-> {
                messages.add(m);
                return ServerResponse.ok()
                        .contentType(APPLICATION_JSON)
                        .body(fromObject(messages));
            } );
            return res;
        };
    }

    private HandlerFunction<ServerResponse> renderMessages() {
        return request -> {
            final List<Message> messages = new ArrayList<>();
            messages.add(new Message("aaa", "bbb"));
            messages.add(new Message("aaa", "bbb"));
            messages.add(new Message("ccc", "ddde"));


            return ServerResponse.ok()
                    .contentType(APPLICATION_JSON)
                    .body(fromObject(messages));
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
}
