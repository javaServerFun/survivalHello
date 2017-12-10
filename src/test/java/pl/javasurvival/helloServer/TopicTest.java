package pl.javasurvival.helloServer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopicTest {

    @Test
    public void createdTopicHasNoMessages() {
        Topic topic = Topic.create("nowy");

        assertTrue(topic.messages.isEmpty());
    }


    @Test
    public void createdTopicHasCorrectName() {
        Topic topic = Topic.create("testowy");

        assertEquals(topic.name, "testowy");
    }


    @Test
    public void addedSingleMessageIsVisible() {
        Topic topic = Topic.create("testowy");

        final Topic newTopic = topic.addMessage(new Message("test content", "seba"));

        assertEquals(newTopic.messages.get(0).content, "test content");
    }


    @Test
    public void addedSecondMessageIsVisible() {
        Topic topic = Topic.create("testowy");

        final Topic newTopic = topic
                        .addMessage(new Message("test content", "seba"))
                        .addMessage(new Message("last message", "seba"));


        assertEquals(newTopic.messages.last().content, "last message");
    }

    @Test
    public void afterAddingOldMessageVisible() {
        Topic topic = Topic.create("testowy");

        final Topic newTopic = topic
                .addMessage(new Message("test content", "seba"))
                .addMessage(new Message("last message", "seba"));


        assertEquals(newTopic.messages.get(0).content, "test content");
    }

    @Test
    public void afterAddingTopicNameUnchanged() {
        Topic topic = Topic.create("testowy");

        final Topic newTopic = topic
                .addMessage(new Message("test content", "seba"))
                .addMessage(new Message("last message", "seba"));


        assertEquals(newTopic.name, "testowy");
    }

}