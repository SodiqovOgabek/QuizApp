package uz.jl.ui;

import uz.jl.BaseUtils;
import uz.jl.Colors;
import uz.jl.configs.ApplicationContextHolder;
import uz.jl.enums.AnswerStatus;
import uz.jl.enums.AuthRole;
import uz.jl.enums.QuestionStatus;
import uz.jl.service.QuestionService;
import uz.jl.service.SubjectService;
import uz.jl.service.auth.AuthUserService;
import uz.jl.vo.answer.AnswerCreateVO;
import uz.jl.vo.auth.AuthUserVO;
import uz.jl.vo.auth.Session;
import uz.jl.vo.http.DataVO;
import uz.jl.vo.http.Response;
import uz.jl.vo.question.QuestionCreateVO;
import uz.jl.vo.question.QuestionVO;
import uz.jl.vo.subject.SubjectCreateVO;
import uz.jl.vo.subject.SubjectVO;

import java.util.List;
import java.util.Objects;

import static uz.jl.ui.TeacherUI.updateQuestion;


public class AdminUI {

    static AuthUserService authUserService = ApplicationContextHolder.getBean(AuthUserService.class);
    static QuestionService questionService = ApplicationContextHolder.getBean(QuestionService.class);
    static SubjectService subjectService = ApplicationContextHolder.getBean(SubjectService.class);

    public static void main(String[] args) {

        if (Objects.nonNull(Session.sessionUser)) {
            System.out.println("================= Admin page ==================");
            BaseUtils.println("Show Student List -> 1");
            BaseUtils.println("Show Teacher List -> 2");
            BaseUtils.println("Show Question List -> 3");
            BaseUtils.println("Question create -> 4");
            BaseUtils.println("Question update -> 5");
            BaseUtils.println("Question delete -> 6");
            BaseUtils.println("Set role to User -> 7");
            BaseUtils.println("Change auth info -> 8");
            BaseUtils.println("Subject create -> 9");
            BaseUtils.println("Log out-> l");
            BaseUtils.println("Quit -> q");
            String choice = BaseUtils.readText("choice ? ");
            switch (choice) {
                case "1" -> showStudentList();
                case "2" -> showTeacherList();
                case "3" -> showQuestionList();
                case "4" -> questionCreate();
                case "5" -> updateQuestion();
                case "6" -> questionDelete();
                case "7" -> setRoleToUser();
                case "8" -> changeAuthInfo();
                case "9" -> subjectCreate();
                case "l" -> Session.setSessionUser(null);
                case "q" -> {
                    BaseUtils.println("Bye");
                    System.exit(0);
                }
            }
            main(args);
        }
    }

    private static void subjectCreate() {
        SubjectCreateVO vo = SubjectCreateVO.builder()
                .title(BaseUtils.readText("title ? "))
                .createdBy(Session.sessionUser.getId())
                .build();

        Response<DataVO<Long>> response = subjectService.create(vo);
        print_response(response);
    }


    private static void changeAuthInfo() {

        BaseUtils.println("Change your username -> 1");
        BaseUtils.println("Change your password -> 2");
        String option = BaseUtils.readText("Choose option: ");

        switch (option) {
            case "1" -> StudentUI.changeUserName();
            case "2" -> StudentUI.changePassword();
        }
    }

    private static void setRoleToUser() {
        Long userId = Long.valueOf(BaseUtils.readText("Insert id: "));

        BaseUtils.println("1.ADMIN");
        BaseUtils.println("2.TEACHER");
        BaseUtils.println("3.STUDENT");

        AuthRole role;

        String option = BaseUtils.readText("Choose role: ");

        switch (option) {
            case "1" -> role = AuthRole.ADMIN;
            case "2" -> role = AuthRole.TEACHER;
            default -> role = AuthRole.STUDENT;
        }

        authUserService.setRole(userId, role);
    }

    private static void showQuestionList() {
        BaseUtils.println("Show All question List -> 1");
        BaseUtils.println("Show parameterized question List -> 2");
        BaseUtils.println("default go back");
        String choice = BaseUtils.readText("choice ? ");
        switch (choice) {
            case "1" -> showAllQuestionList();
            case "2" -> ShowParameterizedQuestionList();
        }

    }

    private static void ShowParameterizedQuestionList() {
        BaseUtils.println("Parameterize with subject -> 1");
        BaseUtils.println("Parameterize with subject and level -> 2");
        BaseUtils.println("default go back");
        String choice = BaseUtils.readText("choice ? ");
        switch (choice) {
            case "1" -> parameterizeWithSubject();
            case "2" -> parameterizeWithSubjectAndLevel();
        }
    }

    public static void parameterizeWithSubject() {
        String subject = BaseUtils.readText("Enter subject name: ");
        Response<DataVO<List<QuestionVO>>> responseList = questionService.getAll(subject, null, null);

        if (responseList.getStatus().equals(200)) {
            for (QuestionVO questionVO : responseList.getData().getBody()) {
                BaseUtils.println(questionVO);
            }
        } else print_response(responseList);
    }

    private static void parameterizeWithSubjectAndLevel() {
        String name = BaseUtils.readText("Enter subject name: ");
        BaseUtils.println("1.EASY 2.MEDIUM 3.HARD");
        String choice = BaseUtils.readText("choice ? ");
        QuestionStatus level = null;
        switch (choice) {
            case "1" -> level = QuestionStatus.EASY;
            case "2" -> level = QuestionStatus.MEDIUM;
            case "3" -> level = QuestionStatus.HARD;
        }

        Response<DataVO<List<QuestionVO>>> responseList = questionService.getAll(name, level, null);
        if (responseList.getStatus().equals(200)) {
            for (QuestionVO questionVO : responseList.getData().getBody()) {
                BaseUtils.println(questionVO);
            }
        } else print_response(responseList);
    }

    private static void showAllQuestionList() {
        Response<DataVO<List<QuestionVO>>> all = questionService.getAll();
        if (all.getStatus() != 200) {
            print_response(all);
            return;
        }
        for (QuestionVO questionVO : all.getData().getBody()) {
            BaseUtils.println(questionVO, Colors.YELLOW);
        }
    }

    private static void questionDelete() {
        Long questionId = Long.valueOf(BaseUtils.readText("Enter question id ? "));
        Response<DataVO<Void>> delete = questionService.delete(questionId);

        if (delete.getStatus() != 200)
            print_response(delete);
        else BaseUtils.println("DONE", Colors.YELLOW);

    }

    private static void showStudentList() {
        Response<DataVO<List<AuthUserVO>>> responseList = authUserService.getAll(AuthRole.STUDENT);
        if (responseList.getStatus().equals(200)) {
            for (AuthUserVO authUserVO : responseList.getData().getBody()) {
                BaseUtils.println(authUserVO, Colors.YELLOW);
            }
        } else print_response(responseList);
    }

    private static void showTeacherList() {
        Response<DataVO<List<AuthUserVO>>> responseList = authUserService.getAll(AuthRole.TEACHER);
        if (responseList.getStatus().equals(200)) {
            for (AuthUserVO authUserVO : responseList.getData().getBody()) {
                BaseUtils.println(authUserVO, Colors.YELLOW);
            }
        } else print_response(responseList);
    }

    private static void questionCreate() {

        Response<DataVO<List<SubjectVO>>> subjectListResponse = subjectService.getAll();
        if (subjectListResponse.getStatus() != 200) {
            print_response(subjectListResponse);
            return;
        }
        List<SubjectVO> subjectList = subjectListResponse.getData().getBody();


        for (SubjectVO subjectVO : subjectList) {
            BaseUtils.println(subjectVO);
        }

        String subjectName = BaseUtils.readText("subject name ? ");

        QuestionCreateVO vo = QuestionCreateVO.builder()
                .body(BaseUtils.readText("body ? "))
                .createdBy(Session.sessionUser.getId())
                .subjectName(subjectName)
                .createdBy(Session.sessionUser.getId())
                .build();


        int i = 0;
        System.out.println("Question status:\n");
        for (QuestionStatus value : QuestionStatus.values()) {
            i++;
            System.out.println(i + " " + value);
        }

        String choice = BaseUtils.readText("choice ? ");
        switch (choice) {
            case "1" -> vo.setStatus(QuestionStatus.EASY);
            case "2" -> vo.setStatus(QuestionStatus.MEDIUM);
            case "3" -> vo.setStatus(QuestionStatus.HARD);

            default -> {
                BaseUtils.println("Invalid choice");
                return;
            }

        }

        System.out.println("Answer:\n");

        AnswerCreateVO body1 = new AnswerCreateVO();
        body1.setBody(BaseUtils.readText("Enter the correct answer"));
        body1.setStatus(AnswerStatus.RIGHT);

        AnswerCreateVO body2 = new AnswerCreateVO();
        body2.setBody(BaseUtils.readText("Enter the incorrect answer"));
        body2.setStatus(AnswerStatus.WRONG);

        AnswerCreateVO body3 = new AnswerCreateVO();
        body3.setBody(BaseUtils.readText("Enter the incorrect answer"));
        body3.setStatus(AnswerStatus.WRONG);

        AnswerCreateVO body4 = new AnswerCreateVO();
        body4.setBody(BaseUtils.readText("Enter the incorrect answer"));
        body4.setStatus(AnswerStatus.WRONG);

        vo.setAnswers(List.of(body1, body2, body3, body4));
        Response<DataVO<Long>> dataVOResponse = questionService.create(vo);
        print_response(dataVOResponse);

    }

    public static void print_response(Response response) {
        String color = response.getStatus() != 200 ? Colors.RED : Colors.GREEN;
        BaseUtils.println(BaseUtils.gson.toJson(response), color);
    }


}
