package pl.javasurvival.helloServer;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.vavr.collection.Iterator;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.BufferUnderflowException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WriteTopicTest {
    @Test
    public void saveMessageTest() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonFactory jsonFactory = new JsonFactory(); // or, for data binding, org.codehaus.jackson.mapper.MappingJsonFactory


        final OutputStream os = Files.newOutputStream(Paths.get("build/stream.jsons"));
        JsonGenerator jg = jsonFactory.createGenerator(os, JsonEncoding.UTF8);


        for (int i =0 ; i < 20; i++) {
            Message m = new Message("aaa"+i, "bbb"+(2*i) + "eee");
            objectMapper.writeValue(jg, m);
        }

        os.close();

        final BufferedReader reader =  Files.newBufferedReader(Paths.get("build/stream.jsons"));
        JsonParser parser = jsonFactory.createParser(reader);



        final Iterable<Message> iterable = new Iterable<Message>() {
            @Override
            public java.util.Iterator<Message> iterator() {
                return new Iterator<Message>() {
                    Try<Message> nextMessage = Try.failure(new IllegalStateException());

                    @Override
                    public boolean hasNext() {
                        try {
                            nextMessage = Try.success(objectMapper.readValue(parser, Message.class));
                        } catch (IOException e) {
                            nextMessage = Try.failure(e);
                        }
                        return nextMessage.isSuccess();
                    }

                    @Override
                    public Message next() {
                        return nextMessage.get();
                    }
                };
            }
        };


        List<Message> all = List.ofAll(iterable);
        System.out.println(all);


        os.close();
    }

}
