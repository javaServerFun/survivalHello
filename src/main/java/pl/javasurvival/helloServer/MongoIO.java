package pl.javasurvival.helloServer;

import org.springframework.data.repository.CrudRepository;

public interface MongoIO extends CrudRepository<BoardMessage, Long> {


}
