package uz.jl.vo.subject;

import lombok.Builder;
import uz.jl.vo.GenericVO;


public class SubjectUpdateVO extends GenericVO {
    private String title;

    @Builder(builderMethodName = "childBuilder")
    public SubjectUpdateVO(Long id, String title) {
        super(id);
        this.title = title;
    }
}
