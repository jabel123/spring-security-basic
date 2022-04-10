package com.example.security1.repository;

import com.example.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD 함수를 JpaRepository가 들고있음
// @Repository라는 어노테이션이 없이도 IoC 된다. 이유는 JpaRepository를 상속했기 떄문에..
public interface UserRepository extends JpaRepository<User, Integer>
{
}
