package com.digvijayb.multitenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digvijayb.multitenant.MultitenantApplication.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>{
    
}
