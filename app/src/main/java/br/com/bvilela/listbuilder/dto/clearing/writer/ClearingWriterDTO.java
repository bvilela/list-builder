package br.com.bvilela.listbuilder.dto.clearing.writer;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@Builder
public class ClearingWriterDTO {

    List<ClearingWriterItemDTO> items;

    List<ClearingWriterItemLayout2DTO> itemsLayout2;
}
