package uz.jl.vo.answer;

import uz.jl.enums.AnswerStatus;
import uz.jl.vo.GenericVO;


public class AnswerUpdateVO extends GenericVO {
    private String body;

    private AnswerStatus status;

    public AnswerUpdateVO(Long id, String body, AnswerStatus status) {
        super(id);
        this.body = body;
        this.status = status;
    }
}
