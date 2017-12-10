package pl.javasurvival.helloServer;

import io.vavr.collection.List;
import io.vavr.control.Option;
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

public class MessageBoardApplication {
    private final ForumService forumService = new ForumService();

    private MessageBoardApplication() {

    }

    public static void main(String[] args) {
        new MessageBoardApplication().serve();

    }

    private void serve() {
        RouterFunction route = nest( path("/api"),
                route(GET("/time"), renderTime())
                .andRoute(GET("/messages/{topic}"), renderMessages())
                .andRoute(POST("/messages/{topic}"), postMessage()));

        HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);
        HttpServer server = HttpServer.create("localhost", 8080);
        ReactorHttpHandlerAdapter myReactorHandler = new ReactorHttpHandlerAdapter(httpHandler);
        server.startAndAwait(myReactorHandler);
    }

    private HandlerFunction<ServerResponse> postMessage() {
        return request -> {
            final String topic =request.pathVariable("topic");
            Mono<Message> postedMessage = request.bodyToMono(Message.class);
            return postedMessage.flatMap( message -> {
                final Option<Topic> updatedTopic = forumService.addMessageToTopic(topic, message);
                return messagesOrErrror(updatedTopic);

            });

        };
    }

    private Mono<ServerResponse> messagesOrErrror(Option<Topic> topic) {
        return topic.map ( t ->
            ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(fromObject(t.messages.toJavaList()))
        ).getOrElse( () ->
                ServerResponse.notFound().build()

        );
    }

    private HandlerFunction<ServerResponse> renderMessages() {
        return request -> {
            final String topic =request.pathVariable("topic");
            return messagesOrErrror( this.forumService.getTopicByName(topic));
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
