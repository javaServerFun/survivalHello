package pl.javasurvival.helloServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringContextApp  {
    private final MongoIO mongo;


    @Autowired
    public SpringContextApp(MongoIO mongo) {
        this.mongo = mongo;
    }
}
