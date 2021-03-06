package com.se.tss.ExamOnline.Controller;

import com.se.tss.CourseArrangeMgr.Service.ClassInfoService;
import com.se.tss.CourseArrangeMgr.Service.TeacherInfoService;
import com.se.tss.ExamOnline.Domain.ExamResponseBody;
import com.se.tss.ExamOnline.Domain.GradeResponseBody;
import com.se.tss.ExamOnline.Domain.QuestionResponseBody;
import com.se.tss.ExamOnline.Service.ExamService;
import com.se.tss.ExamOnline.Service.GradeService;
import com.se.tss.ExamOnline.Service.TestService;
import com.se.tss.infomgr.annotation.CurrentUser;
import com.se.tss.infomgr.annotation.LoginRequired;
import com.se.tss.infomgr.model.User;
import com.se.tss.infomgr.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/exam/grades")
public class GradeController {
    private final GradeService gradeService;
    private final TestService testService;
    private final UserRepository userRepository;
    private final ClassInfoService classInfoService;
    private final TeacherInfoService teacherInfoService;
    private final ExamService examService;

    @Autowired
    public GradeController(GradeService gradeService, TestService testService, UserRepository userRepository, ClassInfoService classInfoService, TeacherInfoService teacherInfoService, ExamService examService) {
        this.gradeService = gradeService;
        this.testService = testService;
        this.userRepository = userRepository;
        this.classInfoService = classInfoService;
        this.teacherInfoService = teacherInfoService;
        this.examService = examService;
    }

    @LoginRequired
    @RequestMapping(value = "/history", method = RequestMethod.GET)
    public ResponseEntity<?> getHistory(@CurrentUser User user) {
        if (!userRepository.findAuthorityById(user.getId()).equals("Student")) {
            return ResponseEntity.badRequest().body(new GradeResponseBody("No permission"));
        }
        return ResponseEntity.ok(new GradeResponseBody("Success", gradeService.getHistoryGrade(user.getId())));
    }

    @LoginRequired
    @RequestMapping(value = "/exams", method = RequestMethod.GET)
    public ResponseEntity<?> getExams(@CurrentUser User user) {
        if (!userRepository.findAuthorityById(user.getId()).equals("Teacher")) {
            return ResponseEntity.badRequest().body(new ExamResponseBody("No permission"));
        }
        return ResponseEntity.ok(new ExamResponseBody("Success", gradeService.getExams(classInfoService.getIdByTeacherid(teacherInfoService.findIdByName(user.getName())))));
    }

    @LoginRequired
    @RequestMapping(value = "/statistic", method = RequestMethod.GET)
    public ResponseEntity<?> getStatistic(@CurrentUser User user, Integer eid) {
        if (!userRepository.findAuthorityById(user.getId()).equals("Teacher")) {
            return ResponseEntity.badRequest().body(new GradeResponseBody("No permission"));
        }
        List<GradeResponseBody.ExamGrade> examGrades = new ArrayList<>();
        examGrades.add(gradeService.getStatistic(eid));
        return ResponseEntity.ok(new GradeResponseBody("Success", examGrades));
    }
}
