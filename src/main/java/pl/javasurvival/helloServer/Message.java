package pl.javasurvival.helloServer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    public final String content;
    public final String author;

    @JsonCreator
    public Message(@JsonProperty("content") String content,
                   @JsonProperty("author") String author) {
        this.content = content;
        this.author = author;
    }

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
