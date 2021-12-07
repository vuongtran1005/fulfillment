package com.bluebelt.fulfillment.repository;

import com.bluebelt.fulfillment.model.role.Role;
import com.bluebelt.fulfillment.model.role.RoleName;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

public interface RoleRepository extends BaseRepository<Role, Long>{

    Optional<Role> findByName(@NotBlank RoleName roleName);

}
