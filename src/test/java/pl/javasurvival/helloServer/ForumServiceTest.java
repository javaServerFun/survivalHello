package pl.javasurvival.helloServer;

import io.vavr.collection.List;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForumServiceTest {


    @Test
    public void shouldHaveMinimumThreeTopicsAtStart() {
          final ForumService service = new ForumService();

          assertTrue( service.getTopics().length() >= 3);
    }


    @Test
    public void shouldAddMessageToJavaTopic() {
        final ForumService service = new ForumService();

        final Option<Topic> topic = service.addMessageToTopic("java", new Message("nowy", "tester"));


        assertEquals(topic.get().messages.last().author, "tester");
    }

}