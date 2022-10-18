package uz.jl.vo.answer;

import lombok.*;
import uz.jl.enums.AnswerStatus;
import uz.jl.vo.BaseVO;



@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AnswerCreateVO implements BaseVO {

    private String body;

    private AnswerStatus status;
}
