package br.com.bvilela.listbuilder.dto.audience;

import br.com.bvilela.listbuilder.dto.util.BaseInputDTO;
import br.com.bvilela.listbuilder.validator.AudienceValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class AudienceInputDTO extends BaseInputDTO {
    public void validate() {
        AudienceValidator.validateData(this);
    }
}
