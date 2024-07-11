package de.fuseki.repository;

import de.fuseki.entities.MediaOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaOrderRepository extends JpaRepository<MediaOrder, Integer> {
}
