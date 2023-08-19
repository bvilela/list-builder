package br.com.bvilela.listbuilder.dto.discourse.writer;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiscourseWriterDTO {
    private List<DiscourseWriterItemDTO> send;
    private List<DiscourseWriterItemDTO> receive;
}
