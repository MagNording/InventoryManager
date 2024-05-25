package se.what.inventorymanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.what.inventorymanager.domain.SearchRecord;

@Repository
public interface SearchRecordRepo extends JpaRepository<SearchRecord, Integer> {

    // Lägg till metoder
}
