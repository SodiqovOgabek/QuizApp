package uz.jl.vo.variant;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.jl.enums.QuestionStatus;
import uz.jl.vo.BaseVO;



@Builder
@Getter
@Setter
public class VariantCreateVO implements BaseVO {
    private String subjectName;
    private QuestionStatus level;
    private Integer numberOfQuestions;
    private Long userId;
}
