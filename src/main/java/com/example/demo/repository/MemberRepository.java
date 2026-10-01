package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.Member;


public interface MemberRepository extends JpaRepository<Member, Long> {

    List<Member> findByName(String name);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByPhone(String phone);

    List<Member> findByMemberStatus(Member memberStatus);


    
}
