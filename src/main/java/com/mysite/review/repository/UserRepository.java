package com.mysite.review.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mysite.review.entity.SiteUser;

public interface UserRepository extends JpaRepository<SiteUser, Long> {
	
	Optional<SiteUser> findByusername(String username);

}
 