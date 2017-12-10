package pl.javasurvival.helloServer;

import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;

public class MessageBoardService {
    private Map<String, Topic> topics;

    MessageBoardService() {
        this.topics = List.of( "java", "ogólny", "dziwne")
                .map( name -> Topic.create(name))
                .toMap(topic -> topic.name, topic -> topic );
    }

    synchronized Option<Topic> getTopic(String topicName) {
        return this.topics.get(topicName);
    }

    synchronized Option<Topic> addMessageToTopic(String topicName, Message newMsg) {
        Option<Topic> newTopic = getTopic(topicName).map(topic -> topic.addMessage(newMsg));
        Option<Map<String, Topic>> newTopics = newTopic.map(topic -> this.topics.put(topicName, topic));
        newTopics.forEach( topics -> this.topics = topics );
        return newTopic;
    }
}
