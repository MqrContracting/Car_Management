package com.carwash.CarWash.repository;
import com.carwash.CarWash.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

    // get user by email since emails are unique
    Optional<UserInfo> findByEmail(String email);
}