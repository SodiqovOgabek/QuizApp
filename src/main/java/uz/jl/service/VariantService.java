package uz.jl.service;

import lombok.NonNull;
import uz.jl.configs.ApplicationContextHolder;
import uz.jl.dao.AbstractDAO;
import uz.jl.dao.auth.AuthUserDAO;
import uz.jl.dao.qa.QuestionDAO;
import uz.jl.dao.subject.SubjectDAO;
import uz.jl.dao.variant.VariantDAO;
import uz.jl.domains.QA.AnswerEntity;
import uz.jl.domains.QA.QuestionEntity;
import uz.jl.domains.QA.VariantEntity;
import uz.jl.domains.auth.AuthUser;
import uz.jl.domains.subject.SubjectEntity;
import uz.jl.utils.BaseUtils;
import uz.jl.utils.validators.variantValidators.VariantValidator;
import uz.jl.vo.answer.AnswerVO;
import uz.jl.vo.http.AppErrorVO;
import uz.jl.vo.http.DataVO;
import uz.jl.vo.http.Response;
import uz.jl.vo.question.QuestionVO;
import uz.jl.vo.subject.SubjectVO;
import uz.jl.vo.variant.VariantCreateVO;
import uz.jl.vo.variant.VariantUpdateVO;
import uz.jl.vo.variant.VariantVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class VariantService extends AbstractDAO<VariantDAO> implements GenericCRUDService<
        VariantVO,
        VariantCreateVO,
        VariantUpdateVO,
        Long> {
    private static VariantService instance;
    VariantValidator validator = ApplicationContextHolder.getBean(VariantValidator.class);
    private static AuthUserDAO authUserDAO = ApplicationContextHolder.getBean(AuthUserDAO.class);
    private static QuestionDAO questionDAO = ApplicationContextHolder.getBean(QuestionDAO.class);
    private static SubjectDAO subjectDAO = ApplicationContextHolder.getBean(SubjectDAO.class);

    private VariantService() {
        super(
                ApplicationContextHolder.getBean(VariantDAO.class),
                ApplicationContextHolder.getBean(BaseUtils.class));
    }

    public static VariantService getInstance() {
        if (instance == null) {
            instance = new VariantService();
        }
        return instance;
    }

    @NonNull
    public Response<DataVO<Long>> create(@NonNull VariantCreateVO vo) {
        return null;
    }

    public Response<DataVO<VariantEntity>> createAndGet(@NonNull VariantCreateVO vo) {
        try {
            validator.validOnCreate(vo);
            SubjectEntity subjectEntity = subjectDAO.findByName(vo.getSubjectName());
            AuthUser authUser = authUserDAO.findById(vo.getUserId());

            List<QuestionEntity> questionEntitiesList = questionDAO.findAllBySubjectIdAndLevel(subjectEntity.getId(), vo.getLevel(), vo.getNumberOfQuestions());
            VariantEntity variantEntity = VariantEntity.childBuilder()
                    .questions(questionEntitiesList)
                    .user(authUser)
                    .numberOfRightAnswers(0)
                    .numberOfQuestions(vo.getNumberOfQuestions())
                    .build();

            VariantEntity save = dao.save(variantEntity);


            return new Response<>(new DataVO<>(save), 200);
        } catch (Exception e) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage(e.getMessage())
                    .build()), 400);
        }
    }


    @Override
    public Response<DataVO<Void>> update(@NonNull VariantUpdateVO vo) {
        return null;
    }

    @Override
    public Response<DataVO<Void>> delete(@NonNull Long aLong) {
        return null;
    }

    @Override
    public Response<DataVO<VariantVO>> get(@NonNull Long variantId) {
        VariantEntity variantEntity = dao.findById(variantId);
        if (Objects.isNull(variantEntity))
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("Variant not found by id")
                    .build()), 400);

        VariantVO variantVO = map(variantEntity);

        return new Response<>(new DataVO<>(variantVO), 200);
    }

    @Override
    public Response<DataVO<List<VariantVO>>> getAll() {
        return null;
    }

    public void UpdateVariantEntity(VariantEntity variant) {
        dao.update(variant);
    }

    public Response<DataVO<List<VariantVO>>> getAllByStudentId(Long studentId) {
        List<VariantEntity> all = dao.findByStudentId(studentId);

        if (all.isEmpty()) {
            return new Response<>(new DataVO<>(AppErrorVO.builder()
                    .friendlyMessage("No variants found").build()), 404);
        }

        List<VariantVO> response = new ArrayList<>();
        for (VariantEntity variantEntity : all) {
            VariantVO variantVO = map(variantEntity);
            response.add(variantVO);
        }
        return new Response<>(new DataVO<>(response), 200);
    }

    private static VariantVO map(VariantEntity variantEntity) {
        VariantVO variantVO = VariantVO.childBuilder()
                .id(variantEntity.getId())
                .createdAt(variantEntity.getCreatedAt())
                .status(variantEntity.getStatus())
                .numberOfRightAnswers(variantEntity.getNumberOfRightAnswers()).build();

        List<QuestionVO> questionVOList = new ArrayList<>();

        for (QuestionEntity questionEntity : variantEntity.getQuestions()) {
            SubjectEntity subject = questionEntity.getSubject();

            SubjectVO subjectVO = SubjectVO.childBuilder()
                    .id(subject.getId())
                    .title(subject.getTitle())
                    .build();

            QuestionVO questionVO = QuestionVO.childBuilder()
                    .id(questionEntity.getId())
                    .body(questionEntity.getBody())
                    .status(questionEntity.getStatus())
                    .subject(subjectVO)
                    .build();

            List<AnswerVO> answerVOList = new ArrayList<>();
            for (AnswerEntity answer : questionEntity.getAnswers()) {
                AnswerVO answerVO = AnswerVO.childBuilder()
                        .body(answer.getBody())
                        .id(answer.getId())
                        .status(answer.getStatus())
                        .build();
                answerVOList.add(answerVO);
            }
            questionVO.setAnswers(answerVOList);
            questionVOList.add(questionVO);
        }
        variantVO.setQuestions(questionVOList);
        return variantVO;
    }
}
