package fr.legouloan.gourava_backend.repository;

import fr.legouloan.gourava_backend.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query(value = """
        SELECT t.tag, COUNT(t.tag) AS usage_count 
        FROM tags t 
        GROUP BY t.tag 
        ORDER BY usage_count DESC 
        LIMIT :limit
        """, nativeQuery = true)
    List<Object[]> findTagsUsageCount(@Param("limit") int limit);
}
