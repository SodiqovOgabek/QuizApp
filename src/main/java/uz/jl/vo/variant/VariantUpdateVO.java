package uz.jl.vo.variant;

import lombok.Builder;
import uz.jl.domains.auth.AuthUser;
import uz.jl.enums.QuestionStatus;
import uz.jl.vo.GenericVO;
import uz.jl.vo.question.QuestionVO;

import java.util.List;

public class VariantUpdateVO extends GenericVO {
    private AuthUser user;
    private QuestionStatus status;
    private List<QuestionVO> questions;
    private Integer numberOfRightAnswers;

    @Builder(builderMethodName = "childBuilder")
    public VariantUpdateVO(Long id, AuthUser user, QuestionStatus status, List<QuestionVO> questions, Integer numberOfRightAnswers) {
        super(id);
        this.user = user;
        this.status = status;
        this.questions = questions;
        this.numberOfRightAnswers = numberOfRightAnswers;
    }
}
