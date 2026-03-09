package com.company.platform.identityaccess.infrastructure.repository;

import com.company.platform.identityaccess.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query("""
                SELECT p.name
                FROM User u
                JOIN UserRole ur ON ur.user.id = u.id
                JOIN RolePermission rp ON rp.role.id = ur.role.id
                JOIN Permission p ON p.id = rp.permission.id
                WHERE u.username = :username
            """)
    List<String> findPermissionsByUsername(@Param("username") String username);

    @Query("""
                SELECT r.name
                FROM User u
                JOIN UserRole ur ON ur.user.id = u.id
                JOIN Role r ON r.id = ur.role.id
                WHERE u.username = :username
            """)
    List<String> findRoleNamesByUsername(@Param("username") String username);
}
