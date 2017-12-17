package pl.javasurvival.helloServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringIni {

    private final MongoRepository mongo;

    @Autowired
    public SpringIni(MongoRepository mongo) {
        this.mongo = mongo;
    }
}
