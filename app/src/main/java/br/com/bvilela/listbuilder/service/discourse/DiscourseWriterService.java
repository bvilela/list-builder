package br.com.bvilela.listbuilder.service.discourse;

import br.com.bvilela.listbuilder.dto.discourse.InputDiscourseDTO;
import java.nio.file.Path;

public interface DiscourseWriterService {

    Path writerPDF(InputDiscourseDTO dto);
}
