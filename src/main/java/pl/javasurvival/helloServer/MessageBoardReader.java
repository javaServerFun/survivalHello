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
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MessageBoardReader {


    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonFactory jsonFactory = new JsonFactory();




    Map<String, Topic> readAllTopics(String path) {
        final BufferedReader reader;
        try {
            reader = Files.newBufferedReader(Paths.get(path));
            JsonParser parser = jsonFactory.createParser(reader);
            final Iterable<BoardMessage> iterableReader = new Iterable<BoardMessage>() {
                @Override
                public java.util.Iterator<BoardMessage> iterator() {
                    return new Iterator<BoardMessage>() {
                        Try<BoardMessage> nextMessage = Try.failure(new IllegalStateException());

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
                }
            };

            return List.ofAll(iterableReader).foldLeft(HashMap.<String, Topic>empty(), (map, msg) ->
                    map.put( msg.topic,
                            map.get(msg.topic)
                                    .getOrElse(Topic.create(msg.topic))
                                    .addMessage(msg.message)));


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }



}


