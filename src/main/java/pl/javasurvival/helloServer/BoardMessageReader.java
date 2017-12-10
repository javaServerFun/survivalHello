package pl.javasurvival.helloServer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.collection.HashMap;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Try;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class BoardMessageReader {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonFactory jsonFactory = new JsonFactory();


    Map< String, Topic> readAllTopics(String path) {
        try {
            BufferedReader reader = Files.newBufferedReader(Paths.get(path));
            JsonParser parser = jsonFactory.createParser(reader);

            final Iterable<BoardMessage> iterableRead = () ->
                    new Iterator<BoardMessage>() {
                        Try<BoardMessage> nextMessage = Try.failure( new IllegalStateException());

                        @Override
                        public boolean hasNext() {
                            try {
                                nextMessage = Try.success(objectMapper.readValue(parser, BoardMessage.class));
                            } catch (IOException e) {
                                nextMessage = Try.failure(e);
                            }

                            return nextMessage.isSuccess();
                        }

                        @Override
                        public BoardMessage next() {
                            return nextMessage.get();
                        }
                    };

            return List.ofAll(iterableRead)
                    .foldLeft(HashMap.<String, Topic>empty(),
                            (existingMap, newElement) ->
                                    existingMap.put( newElement.topic,
                                            existingMap.get( newElement.topic)
                                                    .getOrElse(Topic.create(newElement.topic))
                                                    .addMessage(newElement.message))
                                    );



        } catch (IOException e) {
            return HashMap.<String, Topic>empty();
        }

    }
}
