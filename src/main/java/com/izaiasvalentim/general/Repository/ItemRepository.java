package com.izaiasvalentim.general.Repository;

import com.izaiasvalentim.general.Domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findFirstByName(String name);

    Optional<Item> findByBatch(String batch);

    Optional<List<Item>> findAllByName(String name);
}
