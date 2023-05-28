package br.com.bvilela.listbuilder.exception.listtype;

import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import java.io.Serial;
import javax.validation.constraints.NotNull;

public class ServiceListTypeNotFoundException extends Exception {

    @Serial private static final long serialVersionUID = 3052723584627020914L;

    public ServiceListTypeNotFoundException(@NotNull ListTypeEnum listType) {
        super(String.format("Nenhum Processador definido para a lista: %s", listType.toString()));
    }
}
