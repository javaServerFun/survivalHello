package pl.javasurvival.helloServer;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MessageBoardWriter {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OutputStream outputStream;
    private final JsonFactory jsonFactory = new JsonFactory();

    private final JsonGenerator jsonGenerator;

    MessageBoardWriter(String path) {
        try {
            this.outputStream = Files.newOutputStream(Paths.get(path), StandardOpenOption.APPEND, StandardOpenOption.CREATE);
             jsonGenerator = jsonFactory.createGenerator(outputStream, JsonEncoding.UTF8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void write(String topic, Message message) {
        final BoardMessage msg = new BoardMessage(topic, message);
        try {
            objectMapper.writeValue(jsonGenerator, msg);
            this.outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }






}
