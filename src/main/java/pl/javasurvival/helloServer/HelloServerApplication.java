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
import java.util.Optional;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class HelloServerApplication {

    public static void main(String[] args) {
        HelloServerApplication application = new HelloServerApplication();
        application.serve();
    }

    private void serve () {
        RouterFunction route = route(GET("/"),
                request -> {
                    String html = renderWelcome(request.queryParam("imie"));
                    return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(fromObject(html));
                });

        HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);
        HttpServer server = HttpServer.create("localhost", 8080);
        ReactorHttpHandlerAdapter myReactorHandler = new ReactorHttpHandlerAdapter(httpHandler);
        server.startAndAwait(myReactorHandler);
    }

    private String renderWelcome(Optional<String> nameInput) {
        String welcome = "Witaj na stronie obozu przetrwania";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = "\nCzas to:" + now.format(myFormatter);

        String inputHtml = "<input type='text' name='imie'><input type='submit' value='wyślij'>";
        String userHtml = nameInput
                .map(name -> String.format("<p>%s</p>", name))
                .orElse(inputHtml);

        String htmlTemplate = "<body>" +
                "<h1>%s</h1>" +
                "<p>%s</p>"+
                "<form action='?'>" +
                userHtml +
                "</form>" +
                "</body>";

        String html = String.format(htmlTemplate, welcome, time);
        return html;

    }

}
