package com.bruno.listbuilder.service.discurso.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.bruno.listbuilder.config.AppProperties;
import com.bruno.listbuilder.dto.discurso.DiscursoAllThemesDTO;
import com.bruno.listbuilder.dto.discurso.FileInputDataDiscursoDTO;
import com.bruno.listbuilder.dto.discurso.FileInputDataDiscursoItemDTO;
import com.bruno.listbuilder.enuns.ListTypeEnum;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.BaseGenerateService;
import com.bruno.listbuilder.service.ConvertImageService;
import com.bruno.listbuilder.service.discurso.DiscursoWriterService;
import com.bruno.listbuilder.utils.AppUtils;
import com.bruno.listbuilder.utils.DateUtils;
import com.bruno.listbuilder.utils.FileUtils;
import com.bruno.listbuilder.validator.DiscursoValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DiscursoGenerateServiceImpl implements BaseGenerateService {

	private AppProperties properties;

	private DiscursoWriterService writerService;
	private ConvertImageService convertImageService;

	public DiscursoGenerateServiceImpl(AppProperties properties, DiscursoWriterService writerService,
			ConvertImageService convertImageService) {
		this.properties = properties;
		this.writerService = writerService;
		this.convertImageService = convertImageService;
	}

	@Override
	public ListTypeEnum getListType() {
		return ListTypeEnum.DISCURSO;
	}

	@Override
	public void generateList() throws ListBuilderException {
		try {
			logInit(log);
			
			var dto = getFileInputDataDTO(properties, FileInputDataDiscursoDTO.class);

			Path pathAllThemesFile = Paths.get(properties.getInputDir(), "dados-discurso-temas.json");
			var allThemesDto = FileUtils.readInputFile(pathAllThemesFile, DiscursoAllThemesDTO.class);

			DiscursoValidator.validAllThemesFile(allThemesDto);
			DiscursoValidator.validFileInputData(dto);
			setThemesByNumberAndConvertDate(dto.getReceive(), "Lista Receber", allThemesDto);
			setThemesByNumberAndConvertDate(dto.getSend(), "Lista Enviar", allThemesDto);

			var pathPdf = writerService.writerPDF(dto);
			
			convertImageService.convertToImage(pathPdf);

			logFinish(log);

		} catch (Exception e) {
			throw defaultListBuilderException(e);
		}
	}

	private static void setThemesByNumberAndConvertDate(List<FileInputDataDiscursoItemDTO> list, String message,
			DiscursoAllThemesDTO allThemesDto) throws ListBuilderException {
		
		log.info("{} - Obtendo Títulos dos Discursos pelo número do tema", message);
		
		if (AppUtils.listIsNullOrEmpty(list)) {
			log.info("{} - Lista Vazia! Sem temas para obter.", message);
			return;
		}
		
		for (FileInputDataDiscursoItemDTO item : list) {
			setThemeTitleByThemeNumber(allThemesDto, item);
			item.setDateConverted(DateUtils.parse(item.getDate()));
		}
		
		log.info("Temas obtidos com sucesso!");
	}

	private static void setThemeTitleByThemeNumber(DiscursoAllThemesDTO allThemesDto, FileInputDataDiscursoItemDTO item)
			throws ListBuilderException {
		var themeTitleMissing = Objects.isNull(item.getThemeTitle()) || item.getThemeTitle().isEmpty();
		if (Objects.nonNull(item.getThemeNumber()) && themeTitleMissing) {
			var title = allThemesDto.getThemes().get(Integer.parseInt(item.getThemeNumber()));
			if (Objects.isNull(title)) {
				throw new ListBuilderException("Nenhum tema encontrada para o Número: %s", item.getThemeNumber());
			}
			item.setThemeTitle(title);
		}
	}

}
