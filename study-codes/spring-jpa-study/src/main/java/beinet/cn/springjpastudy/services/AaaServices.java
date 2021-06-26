package beinet.cn.springjpastudy.services;

import beinet.cn.springjpastudy.repository.Aaa;
import beinet.cn.springjpastudy.repository.AaaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AaaServices {
    private final AaaRepository aaaRepository;

    public AaaServices(AaaRepository aaaRepository) {
        this.aaaRepository = aaaRepository;
    }

    public List<Aaa> findAll() {
        return aaaRepository.findAll();
    }

    public List<Aaa> findAllByIdIn(List<Long> ids) {
        return aaaRepository.findAllByIdIn(ids);
    }

    public List<Aaa> findAllByIdIn2(List<Long> ids) {
        return aaaRepository.findAllByIdIn2(ids);
    }
    public Optional<Aaa> findById(Long id) {
        return aaaRepository.findById(id);
    }

    public int deleteByIdIn(List<Long> ids) {
        return aaaRepository.deleteByIdIn(ids);
    }

    public void save(Aaa aaa) {
        aaaRepository.save(aaa);
    }

    public List<Aaa> findByCond(Integer dishhour,
                                LocalDateTime begin,
                                LocalDateTime end) {
        Specification<Aaa> cond = new Specification<Aaa>() {
            @Override
            public Predicate toPredicate(Root<Aaa> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                // like语法
                //if (!StringUtils.isEmpty(name)) {
                //    Predicate pre = criteriaBuilder.like(root.get("name"), "%" + name + "%");
                //    predicates.add(pre);
                //}
                if (begin != null) {
                    Predicate pre = criteriaBuilder.greaterThan(root.get("creationTime"), begin);
                    predicates.add(pre);
                }
                if (end != null) {
                    Predicate pre = criteriaBuilder.lessThanOrEqualTo(root.get("creationTime"), end);
                    predicates.add(pre);
                }
                if (dishhour != null) {
                    Predicate pre = criteriaBuilder.ge(root.get("dishhour"), dishhour);
                    predicates.add(pre);
                }

                Predicate[] pre = predicates.toArray(new Predicate[0]);
                return criteriaBuilder.and(predicates.toArray(pre));
            }
        };
        return aaaRepository.findAll(cond);
    }
}
