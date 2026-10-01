package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.model.Member;
import com.example.demo.repository.MemberRepository;

@Service
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // Persists a newly registered member.
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    // Updates the core profile fields for an existing member.
    public Member updateMember(Long id, Member updatedMember) {
        return memberRepository.findById(id)
                .map(member -> {
                    member.setName(updatedMember.getName());
                    member.setEmail(updatedMember.getEmail());
                    member.setPhone(updatedMember.getPhone());
                    member.setAddress(updatedMember.getAddress());
                    member.setMembershipDate(updatedMember.getMembershipDate());
                    member.setMemberStatus(updatedMember.getMemberStatus());
                    return memberRepository.save(member);
                })
                .orElse(null);
    }

    // Reads one member by ID.
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    // Returns all members in the library database.
    public Iterable<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // Removes a member from the system.
    public void deleteMember(Long id) {
        memberRepository.deleteById(id);
    }
}
