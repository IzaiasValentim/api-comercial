package com.izaiasvalentim.general.Repository;

import com.izaiasvalentim.general.Domain.ApiUser;
import com.izaiasvalentim.general.Domain.BaseUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiUserRepository extends JpaRepository<ApiUser, Long> {
    Optional<ApiUser> findByUser(BaseUser user);
}
