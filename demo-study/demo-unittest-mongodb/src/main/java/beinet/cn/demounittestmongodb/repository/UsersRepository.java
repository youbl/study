package beinet.cn.demounittestmongodb.repository;

import beinet.cn.demounittestmongodb.entity.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UsersRepository extends MongoRepository<Users, Long> {
    Users findTopByNameOrderByIdDesc(String name);
}
