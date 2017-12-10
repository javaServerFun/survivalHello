package pl.javasurvival.helloServer;

import io.vavr.collection.List;

public class Topic {
    public final String name;

    public final List<Message> messages;

    public Topic(String name, List<Message> messages) {
        this.name = name;
        this.messages = messages;
    }
}
