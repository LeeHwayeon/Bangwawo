package com.ssafy.banggawawo.controller;

import com.ssafy.banggawawo.domain.dto.VolunteerDto;
import com.ssafy.banggawawo.domain.entity.Volunteer;
import com.ssafy.banggawawo.service.JwtService;
import com.ssafy.banggawawo.service.VolunteerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Api(value = "봉사자 관련 정보 처리")
@RequiredArgsConstructor
@RequestMapping(value = "/volunteer", produces = {MediaType.APPLICATION_JSON_VALUE})
@RestController
public class VolunteerController {
    private final VolunteerService volunteerService;
    private final JwtService jwtService;

    @ApiOperation(value="봉사자 정보 조회", notes="봉사자 id(vId)를 입력받아 봉사자 정보 제공")
    @GetMapping("/{id}")
    public Map<String, Object> findByVolunteerId(
            @ApiParam(value="봉사자 id", required=true, example="1")
            @PathVariable("id") Long id){
        Map<String, Object> response = new HashMap<>();
        try{
            Optional<Volunteer> oVolunteer = volunteerService.findById(id);
            if(oVolunteer.isPresent()){
                response.put("result", "SUCCESS");
                response.put("type", "VOLUNTEER");
                response.put("user", new VolunteerDto(oVolunteer.get()));
            }else{
                throw new Exception("일치하는 회원정보가 존재하지 않습니다.");
            }
        }catch (Exception e){
            e.printStackTrace();
            response.put("result", "FAIL");
            response.put("reason", e.getMessage());
        }
        return response;
    }

    @ApiOperation(value="봉사자 회원가입", notes="봉사자 정보를 입력받아 회원가입 후 JWT토큰 발급")
    @PostMapping("/")
    public Map<String, Object> saveVolunteer(@RequestBody VolunteerDto value){
        Map<String, Object> response = new HashMap<>();

        try{
            VolunteerDto volunteer = new VolunteerDto(volunteerService.create(value));
            response.put("result", "SUCCESS");
            response.put("JWT", jwtService.create("Volunteer", volunteer));
        }catch(Exception e){
            response.put("result", "FAIL");
            response.put("reason", "회원가입 실패");
            e.printStackTrace();
        }
        return response;
    }

    @ApiOperation(value="봉사자 정보 수정", notes="봉사자 id와 자기소개를 입력받아 그대로 내용 수정\n" +
            "vId : 봉사자 id, introduce : 수정할 자기소개 내용")
    @PutMapping("/")
    public Map<String, Object> updateIntroduce(@RequestBody Map<String, Object> request){
        Long vId = Long.parseLong(request.get("vId").toString());
        String introduce = request.get("introduce").toString();
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Volunteer> oVolunteer = volunteerService.findById(vId);
            if (oVolunteer.isPresent()) {
                Volunteer volunteer = oVolunteer.get();
                volunteer.setIntroduce(introduce);
                Volunteer result = volunteerService.save(volunteer);
                response.put("result", "SUCCESS");
                response.put("user", new VolunteerDto(result));
            } else {
                throw new Exception("일치하는 회원정보가 존재하지 않습니다.");
            }
        }catch (Exception e){
            System.out.println("에러 메세지 : " + e.getMessage());
            e.printStackTrace();
            response.put("result", "FAIL");
            response.put("reason", e.getMessage());
        }
        return response;
    }

    @ApiOperation(value="봉사자 정보 삭제", notes="봉사자 id를 받아 삭제")
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteVolunteer(@PathVariable("id") Long id){
        Map<String, Object> response = new HashMap<>();

        try{
            Optional<Volunteer> Volunteer = volunteerService.findById(id);
            if(Volunteer.isPresent()){
                volunteerService.delete(Volunteer.get());
                response.put("result", "SUCCESS");
            }else{
                throw new Exception("일치하는 회원정보가 존재하지 않습니다.");
            }
        }catch (Exception e){
            System.out.println("에러 메세지 : " + e.getMessage());
            e.printStackTrace();
            response.put("result", "FAIL");
            response.put("reason", "해당 봉사자가 존재하지 않습니다.");
        }
        return response;
    }

    @ApiOperation(value="대화 가능 봉사자 정보 조회", notes="현재 대화가 가능한 봉사자 리스트 제공 \ntalkable=true인 봉사자 목록을 가져온다.")
    @GetMapping("/talkable")
    public Map<String, Object> findByVolunteerTalkable(){
        Map<String, Object> response = new HashMap<>();
        try{
            List<Volunteer> volunteers = volunteerService.findByTalkable();
            response.put("result", "SUCCESS");
            response.put("volunteers", volunteers);
        }catch (Exception e){
            e.printStackTrace();
            response.put("result", "FAIL");
            response.put("reason", "봉사자 정보 조회 실패");
        }
        return response;
    }

    @ApiOperation(value="봉사자 대화가능 상태 변경", notes="봉사자 id를 입력받아 봉사자의 대화 가능 여부를 변경한다. \ntrue면 false, false면 true로 변경하며, 변경 후 결과값을 talkable로 반환")
    @PutMapping("/talkable/{id}")
    public Map<String, Object> updateTalkable(@PathVariable("id") Long id){
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Volunteer> oVolunteer = volunteerService.findById(id);
            if (oVolunteer.isPresent()) {
                Volunteer volunteer = oVolunteer.get();
                volunteer.setTalkable(!volunteer.isTalkable());
                Volunteer result = volunteerService.save(volunteer);
                response.put("result", "SUCCESS");
                response.put("talkable", result.isTalkable());
            } else {
                throw new Exception("일치하는 회원정보가 존재하지 않습니다.");
            }
        }catch (Exception e){
            System.out.println("에러 메세지 : " + e.getMessage());
            e.printStackTrace();
            response.put("result", "FAIL");
            response.put("reason", e.getMessage());
        }
        return response;
    }

}
