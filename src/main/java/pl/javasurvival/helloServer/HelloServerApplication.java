package pl.javasurvival.helloServer;

import io.vavr.collection.List;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.ipc.netty.http.server.HttpServer;

import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

public class HelloServerApplication {

    private List<String> guests = List.empty();

    public static void main(String[] args) {
        HelloServerApplication application = new HelloServerApplication();
        application.serve();
    }

    private void serve () {
        RouterFunction route = route(GET("/"),
                request -> {
                    String html = renderPage(request.queryParam("imie"));
                    return ServerResponse.ok().contentType(new MediaType(MediaType.TEXT_HTML, Charset.forName("utf-8"))).body(fromObject(html));
                });

        HttpHandler httpHandler = RouterFunctions.toHttpHandler(route);
        HttpServer server = HttpServer.create("localhost", 8080);
        ReactorHttpHandlerAdapter myReactorHandler = new ReactorHttpHandlerAdapter(httpHandler);
        server.startAndAwait(myReactorHandler);
    }

    private String renderPage(Optional<String> nameInput) {
        String welcome = "Witaj na stronie obozu przetrwania";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter myFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = "\nCzas to:" + now.format(myFormatter);

        String inputHtml = "<input type='text' name='imie'><input type='submit' value='wyślij'>";
        String userHtml = nameInput
                .map(name -> {
                    guests = guests.append(name);
                    return String.format("<p>%s</p>", name);
                })
                .orElse(inputHtml);

        String htmlTemplate = "<body>" +
                "<h1>%s</h1>" +
                "<p>%s</p>"+
                "<form action='?'>" +
                userHtml +
                "<p>Goście</p>" +
                renderGuests() +
                "</form>" +
                "</body>";

        String html = String.format(htmlTemplate, welcome, time);
        return html;
    }

    private String renderGuests() {
        return "<ul>"+
                guests.map(guest -> String.format("<li>%s</li>", guest) ).mkString()
                +"</ul>";


    }

}
