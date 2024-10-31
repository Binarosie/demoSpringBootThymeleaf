package nhuquynh.demothymeleaf.service;

import nhuquynh.demothymeleaf.entity.Category;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface CategoryService {

    long count();


    void deleteById(Long id);

    List<Category> findAll();

    List<Category> findAll(Sort sort);

    <S extends Category> Page<S> findAll(Example<S> example, Pageable pageable);

    Page<Category> findByNameContaining(String name, Pageable pageable);

    Page<Category> findAll(Pageable pageable);

    Optional<Category> findByName(String name);

    <S extends Category> S save(S entity);


    Optional<Category> findById(Long id);

    Page<Category> findByCategoryNameContaining(String name, Pageable pageable);
}
