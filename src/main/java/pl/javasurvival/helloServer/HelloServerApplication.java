package pl.javasurvival.helloServer;

import org.springframework.http.MediaType;
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
		        String welcomeHtml = "<h1>Witaj na stronie obozu przetrwania</h1>";
				LocalDateTime now = LocalDateTime.now();
				DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
				String time = "<p>Czas to:"+ now.format(myFormatter)+"</p>";
				String visits = "<p>To są "+ counter.incrementAndGet() + " odwiedziny</p>";
				String inputHtml = "<input type='text' name='userName'>";

				return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(fromObject("<body>"
                        + welcomeHtml + time + visits + inputHtml
                        + "</body>"
                ));
			});

		HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);
		HttpServer server = HttpServer.create("localhost", 8080);
		ReactorHttpHandlerAdapter myReactorHandler = new ReactorHttpHandlerAdapter(httpHandler);
		server.startAndAwait( myReactorHandler);
	}
}
