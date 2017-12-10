package pl.javasurvival.helloServer;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;

public class MessageBoardService {
    public static final String FILE_NAME = "messages.jsons";
    private Map<String, Topic> topics;

    private final BoardMessageWriter writer = new BoardMessageWriter(FILE_NAME);

    MessageBoardService() {
        this.topics= new BoardMessageReader().readAllTopics(FILE_NAME);
    }

    synchronized Option<Topic> getTopic(String topicName) {
        return this.topics.get(topicName);
    }

    synchronized Option<Topic> addMessageToTopic(String topicName, Message newMsg) {
        Option<Topic> newTopic = getTopic(topicName).map(topic -> topic.addMessage(newMsg));
        newTopic.forEach( topic -> writer.write(topic.name, newMsg));
        Option<Map<String, Topic>> newTopics = newTopic.map(topic -> this.topics.put(topicName, topic));
        newTopics.forEach( topics -> this.topics = topics );
        return newTopic;
    }
}
