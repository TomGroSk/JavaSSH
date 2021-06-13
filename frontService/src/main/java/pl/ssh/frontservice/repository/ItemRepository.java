package pl.ssh.frontservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ssh.frontservice.model.Item;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> getAllByCustomer_Id(Long id);
    List<Item> getAllByCustomer_IdAndItemIdAndItemType(Long id, UUID itemId, String itemType);
}
