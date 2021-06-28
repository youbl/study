package beinet.cn.springjpastudy.services;

import beinet.cn.springjpastudy.repository.Aaa;
import beinet.cn.springjpastudy.repository.AaaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
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

    public Page<Aaa> findByCond(Integer dishhour,
                                LocalDateTime begin,
                                LocalDateTime end,
                                List<Integer> dishIdList,
                                Integer pageNum,
                                Integer pageSize) {
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
                    // 子条件的or
                    Predicate pre1 = criteriaBuilder.ge(root.get("dishhour"), dishhour);
                    Predicate pre2 = criteriaBuilder.ge(root.get("dishId"), dishhour);
                    Predicate pre = criteriaBuilder.or(pre1, pre2);
                    predicates.add(pre);
                }

                if (dishIdList != null) {
                    CriteriaBuilder.In<Integer> pre = criteriaBuilder.in(root.get("dishId"));
                    for (Integer item : dishIdList) {
                        pre.value(item);
                    }
                    predicates.add(pre);
                }

                // 注意Aaa必须添加JoinColumn注解
                /* 生成的SQL如下：
select aaa0_.id as id1_0_, aaa0_.creationTime as creation2_0_, aaa0_.dishId as dishid3_0_, aaa0_.dishhour as dishhour4_0_, aaa0_.num as num5_0_, aaa0_.restId as restid6_0_
from Aaa aaa0_
left outer join AaaChild bbb1_ on aaa0_.id=bbb1_.aaaId
where aaa0_.creationTime>? and (aaa0_.dishhour>=11 or aaa0_.dishId>=11)
  and (bbb1_.childName like ?)
order by aaa0_.creationTime desc, aaa0_.id desc limit ?

注意：由于添加了 JoinColumn注解，得到的List数组，每条记录会再查询一次：
select * from AaaChild bbb0_ where bbb0_.aaaId=?
                * */
                Predicate tmp = criteriaBuilder.like(root.join("bbb", JoinType.LEFT).get("childName"), "%abc%");
                predicates.add(tmp);

                Predicate[] pre = predicates.toArray(new Predicate[0]);
                return criteriaBuilder.and(predicates.toArray(pre));
            }
        };
        if (pageNum == null || pageNum < 0)
            pageNum = 0;
        if (pageSize == null || pageSize <= 0)
            pageSize = 5;

        // order by creationTime desc, id desc
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.Direction.DESC, "creationTime", "id");
        return aaaRepository.findAll(cond, pageable);
        // 最终生成的语句：
        // from Aaa
        // where creationTime>?
        // and (dishhour>=11 or dishId>=11)
        // and (dishId in (13 , 12))
        // order by creationTime desc, id desc limit ?
    }
}
