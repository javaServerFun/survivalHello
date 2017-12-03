package pl.javasurvival.helloServer;

import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.ipc.netty.http.server.HttpServer;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class HelloServerApplication {
    private AtomicInteger counter = new AtomicInteger(0);

    public static void main(String[] args) {
        new HelloServerApplication().serve();

    }

    private void serve() {
        RouterFunction route = route(GET("/"),
                renderWelcome());

        HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);
        HttpServer server = HttpServer.create("localhost", 8080);
        ReactorHttpHandlerAdapter myReactorHandler = new ReactorHttpHandlerAdapter(httpHandler);
        server.startAndAwait(myReactorHandler);
    }

    private HandlerFunction<ServerResponse> renderWelcome() {
        return request -> {
            Optional<String> userName = request.queryParam("userName");
            String welcomeHtml = String.format("<h1>Witaj %s na stronie obozu przetrwania</h1>",
                    userName.orElse("nieznany"));
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            String time = "<p>Czas to:" + now.format(myFormatter) + "</p>";
            String visits = "<p>To są " + counter.incrementAndGet() + " odwiedziny</p>";
            String inputHtml = "<input type='text' name='userName'>";
            String submitHtml = "<input type='submit' value='wyślij'>";
            String formHtml = String.format("<form>%s %s</form>", inputHtml, submitHtml);

            return ServerResponse.ok()
                    .contentType(new MediaType(MediaType.TEXT_HTML, Charset.forName("utf-8")))
                    .body(fromObject("<body>"
                            + welcomeHtml + time + visits + formHtml
                            + "</body>"
                    ));
        };
    }
}
