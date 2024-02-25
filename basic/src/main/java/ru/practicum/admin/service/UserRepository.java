package ru.practicum.admin.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.admin.model.User;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.id in :ids")
    List<User> getAllUserContainsAndLimitNotQueringMethod(@Param("ids") List<Long> ids, Pageable reqPage);

    @Query("select u from User u")
    List<User> getAllLimitNoQueringMethod(Pageable reqPage);
}
