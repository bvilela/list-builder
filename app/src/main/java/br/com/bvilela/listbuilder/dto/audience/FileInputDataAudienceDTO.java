package br.com.bvilela.listbuilder.dto.audience;

import br.com.bvilela.listbuilder.dto.BaseFileInputDataDTO;
import br.com.bvilela.listbuilder.validator.AudienceValidator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class FileInputDataAudienceDTO extends BaseFileInputDataDTO {
    public void validate() {
        AudienceValidator.validateData(this);
    }
}