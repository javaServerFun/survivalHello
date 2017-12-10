package pl.javasurvival.helloServer;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;

public class ForumService {

    private Map<String, Topic> topics;


    ForumService() {
        this.topics = List.of(
                Topic.create("ogólne"),
                Topic.create("strona"),
                Topic.create("java")
        ).toMap( (element) -> element.name, element -> element);

    }




    public Option<Topic> getTopicByName(String topicName) {
        throw new UnsupportedOperationException();
    }

    public Option<Topic> addMessageToTopic(String topicName, Message message) {
        throw new UnsupportedOperationException();
    }

    public Set<String> getTopics() {
        return this.topics.keySet();
    }


}
