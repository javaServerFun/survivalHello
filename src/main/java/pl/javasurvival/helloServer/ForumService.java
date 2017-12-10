package pl.javasurvival.helloServer;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;

import java.util.concurrent.atomic.AtomicReference;

public class ForumService {

    private final AtomicReference<Map<String, Topic>> topics;

    private final MessageBoardWriter writer;

    ForumService() {
        this.topics = new AtomicReference<>(new MessageBoardReader().readAllTopics("messages.jsons"));
        this.writer =  new MessageBoardWriter("messages.jsons");
    }

    public Option<Topic> getTopicByName(String topicName) {
        return this.topics.get().get(topicName);
    }

    public Option<Topic> addMessageToTopic(String topicName, Message message) {
        return topics.updateAndGet(topicsMap ->
                topicsMap.get(topicName)
                        .map(topic -> {
                            writer.write(topicName, message);
                            return topicsMap.put(topicName, topic.addMessage(message));
                        })
                        .getOrElse(topicsMap)
        ).get(topicName);
    }

    public Set<String> getTopics() {
        return this.topics.get().keySet();
    }


}
