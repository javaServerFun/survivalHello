package pl.javasurvival.helloServer;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.collection.Set;
import io.vavr.control.Option;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.Month;
import java.util.concurrent.atomic.AtomicReference;

public class ForumService {

    private final AtomicReference<Map<String, Topic>> topics;

    private final MongoIO mongo;

    ForumService() {
        topics = new AtomicReference<>(HashMap.of( "java", Topic.create("java")));
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringContextApp.class);
        mongo = context.getBean(MongoIO.class);

    }

    public Option<Topic> getTopicByName(String topicName) {
        return this.topics.get().get(topicName);
    }

    public Option<Topic> addMessageToTopic(String topicName, Message message) {
        return topics.updateAndGet(topicsMap ->
                topicsMap.get(topicName)
                        .map(topic -> {
                            mongo.save(new BoardMessage(topicName, message));
                            return topicsMap.put(topicName, topic.addMessage(message));
                        })
                        .getOrElse(topicsMap)
        ).get(topicName);
    }

    public Set<String> getTopics() {
        return this.topics.get().keySet();
    }


}
