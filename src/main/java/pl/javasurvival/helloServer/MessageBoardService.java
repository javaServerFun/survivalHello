package pl.javasurvival.helloServer;

import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import io.vavr.collection.Map;
import io.vavr.control.Option;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MessageBoardService {
    public static final String FILE_NAME = "messages.jsons";
    private Map<String, Topic> topics;

    private final MongoRepository mongoRepository;


    MessageBoardService() {
        ApplicationContext context = new AnnotationConfigApplicationContext(SpringIni.class);
        this.mongoRepository = context.getBean(MongoRepository.class);
        this.topics = List.ofAll(mongoRepository.findAll())
                .foldLeft(HashMap.<String, Topic>empty(),
                        (existingMap, newElement) ->
                                existingMap.put( newElement.topic,
                                        existingMap.get( newElement.topic)
                                                .getOrElse(Topic.create(newElement.topic))
                                                .addMessage(newElement.message))
                );
    }

    synchronized Option<Topic> getTopic(String topicName) {
        return this.topics.get(topicName);
    }

    synchronized Option<Topic> addMessageToTopic(String topicName, Message newMsg) {
        Option<Topic> newTopic = getTopic(topicName).map(topic -> topic.addMessage(newMsg));
        newTopic.forEach( topic -> mongoRepository.save(new BoardMessage(newMsg, topicName)));
        Option<Map<String, Topic>> newTopics = newTopic.map(topic -> this.topics.put(topicName, topic));
        newTopics.forEach( topics -> this.topics = topics );
        return newTopic;
    }
}
