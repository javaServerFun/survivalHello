package pl.javasurvival.helloServer;

import org.springframework.data.repository.CrudRepository;

public interface MongoRepository extends CrudRepository<BoardMessage, Long> {
}
