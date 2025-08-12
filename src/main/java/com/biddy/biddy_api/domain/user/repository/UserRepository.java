package com.biddy.biddy_api.domain.user.repository;

import com.biddy.biddy_api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findById(Long bidderId);
}
