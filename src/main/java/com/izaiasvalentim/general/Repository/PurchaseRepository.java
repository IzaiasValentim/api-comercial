package com.izaiasvalentim.general.Repository;


import com.izaiasvalentim.general.Domain.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, UUID> {
}
