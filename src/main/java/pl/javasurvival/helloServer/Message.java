package pl.javasurvival.helloServer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonDeserialize
public class Message {
    public final String author;
    public final String text;

    @JsonCreator
    public Message(@JsonProperty("author")String author, @JsonProperty("text")String text) {
        this.author = author;
        this.text = text;
    }
}
