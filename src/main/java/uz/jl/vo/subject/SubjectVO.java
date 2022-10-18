package uz.jl.vo.subject;

import lombok.*;
import uz.jl.vo.GenericVO;




@Getter
@Setter
@ToString
public class SubjectVO extends GenericVO {
    private String title;

    @Builder(builderMethodName = "childBuilder")
    public SubjectVO(Long id, String title) {
        super(id);
        this.title = title;
    }
}
