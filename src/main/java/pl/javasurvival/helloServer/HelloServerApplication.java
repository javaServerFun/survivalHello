package pl.javasurvival.helloServer;

import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.ipc.netty.http.server.HttpServer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class HelloServerApplication {
    static AtomicInteger counter = new AtomicInteger(0);

	public static void main(String[] args) {

		RouterFunction route = route( GET("/"),
				request -> {
		        String welcome = "Witaj na stronie obozu przetrwania";
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
				String time = "\nCzas to:"+ now.format(myFormatter);
				String visits = "\nTo są "+ counter.incrementAndGet() + " odwiedziny";

				return ServerResponse.ok().body(fromObject(welcome + time + visits));
			});

		HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);
		HttpServer server = HttpServer.create("localhost", 8080);
		ReactorHttpHandlerAdapter myReactorHandler = new ReactorHttpHandlerAdapter(httpHandler);
		server.startAndAwait( myReactorHandler);
	}
}
