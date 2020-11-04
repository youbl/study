package beinet.cn.demomongounittest.repository;

import beinet.cn.demomongounittest.entity.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<Users, Long> {
    Users findTopByNameOrderByIdDesc(String name);
}
