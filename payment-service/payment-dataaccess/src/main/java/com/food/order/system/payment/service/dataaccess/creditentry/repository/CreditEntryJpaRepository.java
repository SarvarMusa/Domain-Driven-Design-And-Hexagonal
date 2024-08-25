package com.food.order.system.payment.service.dataaccess.creditentry.repository;

import com.food.order.system.payment.service.dataaccess.creditentry.entity.CreditEntryEntity;
import com.food.order.system.payment.service.dataaccess.credithistory.entity.CreditHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CreditEntryJpaRepository extends JpaRepository<CreditEntryEntity, UUID> {
    Optional<CreditEntryEntity> findByCustomerId(UUID customerId);

}
