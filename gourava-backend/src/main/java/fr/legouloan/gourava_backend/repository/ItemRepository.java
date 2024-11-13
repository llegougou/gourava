package fr.legouloan.gourava_backend.repository;

import fr.legouloan.gourava_backend.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
    
}