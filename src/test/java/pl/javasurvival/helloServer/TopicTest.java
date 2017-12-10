package pl.javasurvival.helloServer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TopicTest {

    @Test
    public void createdTopicHasNoMessages() {
        Topic topic = Topic.create("nowy");

        assertTrue( topic.messages.isEmpty());
    }


    @Test
    public void createdTopicHasCorrectName() {
        Topic topic = Topic.create("testowy");

        assertEquals( topic.name, "testowy");
    }


}