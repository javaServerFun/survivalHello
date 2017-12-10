package pl.javasurvival.helloServer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BoardMessage {
    public final Message message;
    public final String topic;

    @JsonCreator
    public BoardMessage(
            @JsonProperty("message") Message message,
            @JsonProperty("topic") String topic) {
        this.message = message;
        this.topic = topic;
    }
}
