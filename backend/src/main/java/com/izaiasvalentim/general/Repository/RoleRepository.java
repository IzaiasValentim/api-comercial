package com.izaiasvalentim.general.Repository;

import com.izaiasvalentim.general.Domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
