package br.com.bvilela.listbuilder.dto.audience;

import br.com.bvilela.listbuilder.dto.util.BaseInputDTO;
import br.com.bvilela.listbuilder.validator.AudienceValidator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Generated;

@Data
@Generated
@EqualsAndHashCode(callSuper = true)
public class AudienceInputDTO extends BaseInputDTO {
    public void validate() {
        AudienceValidator.validateData(this);
    }
}
