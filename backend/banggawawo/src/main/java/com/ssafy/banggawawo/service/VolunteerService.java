package com.ssafy.banggawawo.service;

import com.ssafy.banggawawo.domain.dto.ColorDto;
import com.ssafy.banggawawo.domain.dto.VolunteerDto;
import com.ssafy.banggawawo.domain.entity.Character;
import com.ssafy.banggawawo.domain.entity.Student;
import com.ssafy.banggawawo.domain.entity.Volunteer;
import com.ssafy.banggawawo.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;

    @Autowired
    public VolunteerService(VolunteerRepository volunteerRepository) {
        this.volunteerRepository = volunteerRepository;
    }

    @Transactional(readOnly = true) // 조회 시 데이터 변경 방지
    public Optional<Volunteer> findById(Long id){
        return volunteerRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Volunteer> findByKakaoId(String kakaoId) {
        return volunteerRepository.findByKakaoId(kakaoId);
    }

    @Transactional
    public Volunteer save(Volunteer volunteer) {
        return volunteerRepository.save(volunteer);
    }

    @Transactional
    public Volunteer create(VolunteerDto volunteerDto) {
        Volunteer volunteer = Volunteer.builder()
                .kakaoId(volunteerDto.getKakaoId())
                .nickname(volunteerDto.getNickname())
                .ageRange(volunteerDto.getAgeRange())
                .introduce(volunteerDto.getIntroduce())
                .talkable(volunteerDto.isTalkable())
                .character(volunteerDto.getCharacter()).build();
        return volunteerRepository.save(volunteer);
    }

    @Transactional
    public Volunteer delete(Volunteer volunteer) {
        volunteer.setNickname("탈퇴 회원");
        volunteer.setKakaoId("-");
        volunteer.setAgeRange(-1);
        volunteer.setIntroduce("-");
        volunteer.setCharacter(null);
        return volunteerRepository.save(volunteer);
    }

    @Transactional(readOnly = true)
    public List<Volunteer> findByTalkable() {
        return volunteerRepository.findByTalkable(true);
    }

    public Character toClass(ColorDto[] arr){
        return new Character(arr[0].getColor(), arr[1].getColor(), arr[2].getColor(),
                arr[3].getColor(), arr[4].getColor(), arr[5].getColor());
    }
}
