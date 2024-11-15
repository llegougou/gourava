package fr.legouloan.gourava_backend.repository;

import fr.legouloan.gourava_backend.model.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CriteriaRepository extends JpaRepository<Criteria, Long> {

    @Query(value = """
        SELECT c.name, COUNT(c.name) AS usage_count
        FROM criterias c
        GROUP BY c.name
        ORDER BY RAND()
    """, nativeQuery = true)
    List<Object[]> findAllCriteriaUsageCountRandom();

    @Query(value = """
        SELECT c.name, COUNT(c.name) AS usage_count
        FROM criterias c
        GROUP BY c.name
        ORDER BY RAND()
        LIMIT :limit
    """, nativeQuery = true)
    List<Object[]> findLimitedCriteriaUsageCountRandom(@Param("limit") int limit);

    @Query(value = """
        SELECT c.name, COUNT(c.name) AS usage_count
        FROM criterias c
        GROUP BY c.name
        ORDER BY usage_count DESC
    """, nativeQuery = true)
    List<Object[]> findAllCriteriaUsageCount();

    @Query(value = """
        SELECT c.name, COUNT(c.name) AS usage_count
        FROM criterias c
        GROUP BY c.name
        ORDER BY usage_count DESC
        LIMIT :limit
    """, nativeQuery = true)
    List<Object[]> findLimitedCriteriaUsageCount(@Param("limit") int limit);
}
