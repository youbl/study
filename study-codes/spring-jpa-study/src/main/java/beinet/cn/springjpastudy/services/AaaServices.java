package beinet.cn.springjpastudy.services;

import beinet.cn.springjpastudy.repository.Aaa;
import beinet.cn.springjpastudy.repository.AaaRepository;
import org.springframework.stereotype.Service;

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
}
