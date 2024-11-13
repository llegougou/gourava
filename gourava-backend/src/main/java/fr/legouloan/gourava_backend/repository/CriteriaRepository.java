package fr.legouloan.gourava_backend.repository;

import fr.legouloan.gourava_backend.model.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CriteriaRepository extends JpaRepository<Criteria, Long> {

    @Query(value = """
        SELECT c.name, COUNT(c.name) AS usage_count 
        FROM criteria c 
        GROUP BY c.name 
        ORDER BY usage_count DESC 
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findCriteriaUsageCount(@Param("limit") int limit);
}
