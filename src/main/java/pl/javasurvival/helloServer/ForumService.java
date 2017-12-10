package pl.javasurvival.helloServer;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;

import java.util.concurrent.atomic.AtomicReference;

public class ForumService {

    private AtomicReference<Map<String, Topic>> topics;


    ForumService() {
        this.topics = new AtomicReference<>(List.of(
                Topic.create("ogólne"),
                Topic.create("strona"),
                Topic.create("java")
        ).toMap( (element) -> element.name, element -> element));

    }

    public Option<Topic> getTopicByName(String topicName) {
        return this.topics.get().get(topicName);
    }

    public Option<Topic> addMessageToTopic(String topicName, Message message) {
        return topics.updateAndGet( topicsMap ->
           topicsMap.get(topicName)
                   .map( topic -> topicsMap.put(topicName, topic.addMessage(message)))
                   .getOrElse(topicsMap)
        ).get(topicName);
    }

    public Set<String> getTopics() {
        return this.topics.get().keySet();
    }


}
