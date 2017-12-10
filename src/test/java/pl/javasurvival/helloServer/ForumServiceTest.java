package pl.javasurvival.helloServer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForumServiceTest {


    @Test
    public void shouldHaveMinimumThreeTopicsAtStart() {
          final ForumService service = new ForumService();

          assertTrue( service.getTopics().length() >= 3);
    }

}