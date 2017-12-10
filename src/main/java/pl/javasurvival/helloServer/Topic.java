package pl.javasurvival.helloServer;

import io.vavr.collection.List;

public class Topic {
    public final String name;

    public final List<Message> messages;

    public Topic(String name, List<Message> messages) {
        this.name = name;
        this.messages = messages;
    }

    static Topic create(String topicName) {
        return new Topic(topicName, List.empty());
    }

    public Topic addMessage(Message message) {
        return new Topic(this.name, this.messages.append(message));
    }
}
