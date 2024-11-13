package fr.legouloan.gourava_backend.repository;

import fr.legouloan.gourava_backend.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query(value = """
                    SELECT t.name, COUNT(t.name) AS usage_count
            FROM tags t
            GROUP BY t.name
            ORDER BY RAND()
            LIMIT ?
                """, nativeQuery = true)
    List<Object[]> findTagUsageCountRandom(@Param("limit") int limit);

    @Query(value = """
                    SELECT t.name, COUNT(t.name) AS usage_count
            FROM tags t
            GROUP BY t.name
            ORDER BY usage_count DESC
            LIMIT ?
                """, nativeQuery = true)
    List<Object[]> findTagUsageCount(@Param("limit") int limit);
}