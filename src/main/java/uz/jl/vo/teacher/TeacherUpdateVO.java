package uz.jl.vo.teacher;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import uz.jl.vo.GenericVO;
import uz.jl.vo.subject.SubjectVO;

import java.util.List;


@Getter
@Setter
public class TeacherUpdateVO extends GenericVO {
    private String name;
    private String surname;
    private List<SubjectVO> subjectList;

    @Builder
    public TeacherUpdateVO(Long id, String name, String surname, List<SubjectVO> subjectList) {
        super(id);
        this.name = name;
        this.surname = surname;
        this.subjectList = subjectList;
    }
}
