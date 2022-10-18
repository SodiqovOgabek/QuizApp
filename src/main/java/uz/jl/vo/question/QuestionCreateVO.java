package uz.jl.vo.question;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.jl.enums.QuestionStatus;
import uz.jl.vo.BaseVO;
import uz.jl.vo.answer.AnswerCreateVO;

import java.util.List;



@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class QuestionCreateVO implements BaseVO {

    private String body;
    private QuestionStatus status;
    private List<AnswerCreateVO> answers;
    private String subjectName;
    private Long createdBy;
}
