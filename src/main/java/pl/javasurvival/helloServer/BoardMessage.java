package pl.javasurvival.helloServer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BoardMessage {
    public final String topic;
    public final Message message;

    @JsonCreator
    public BoardMessage(
            @JsonProperty("topic") String topic,
            @JsonProperty("message") Message message) {
        this.topic = topic;
        this.message = message;
    }
}
