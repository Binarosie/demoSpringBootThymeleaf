package nhuquynh.demothymeleaf.repository;

import nhuquynh.demothymeleaf.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);

    //tìm và phân trang kết quả tìm được
    Page<Category> findByNameContaining(String name, Pageable pageable);

    //Page<Category> findByCategoryNameContaining(String categoryName, Pageable pageable);
}
