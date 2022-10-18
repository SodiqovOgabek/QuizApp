package uz.jl.vo.student;

import lombok.Getter;
import lombok.Setter;
import uz.jl.domains.auth.AuthUser;
import uz.jl.vo.GenericVO;

@Setter
@Getter
public class StudentVO  extends GenericVO {
    private String name;
    private String surname;
    private AuthUser user;


    public StudentVO(Long id) {
        super(id);
    }
}
