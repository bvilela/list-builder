package br.com.bvilela.listbuilder.utils;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.regex.Pattern;

import br.com.bvilela.listbuilder.enuns.ListTypeEnum;
import org.slf4j.Logger;

import br.com.bvilela.listbuilder.exception.ListBuilderException;

public final class AppUtils {

	private AppUtils() {
	}
	
	private static final float ONE_MM_IN_POINT = 2.83465f;
	private static final int SIZE_SCALE = 20;

	public static String removeAccents(String texto) {
		String nfdNormalizedString = Normalizer.normalize(texto, Normalizer.Form.NFD);
		Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	public static String printList(List<String> list) {
		return list.toString().replace("[", "").replace("]", "");
	}

	public static List<LocalDate> validAndConvertListDates(List<String> list, String msg, Logger log)
			throws ListBuilderException {
		List<LocalDate> listDate = null;

		if (Objects.nonNull(list)) {
			log.info("Validando Datas {}", msg);

			listDate = new ArrayList<>();
			for (String item : list) {
				try {
					listDate.add(DateUtils.parse(item));
				} catch (DateTimeParseException e) {
					throw new ListBuilderException("Valor '%s' não é uma data válida", item);
				}
			}

			log.info("Datas {} validadas com Sucesso!", msg);
		}
		return listDate;
	}

	public static <T> Map<LocalDate, T> validAndConvertMapDates(Map<String, T> map, String msg, Logger log)
			throws ListBuilderException {
		Map<LocalDate, T> mapDate = null;

		if (Objects.nonNull(map)) {
			log.info("Validando Datas {}", msg);

			mapDate = new LinkedHashMap<>();
			for (Entry<String, T> item : map.entrySet()) {
				try {
					mapDate.put(DateUtils.parse(item.getKey()), item.getValue());
				} catch (DateTimeParseException e) {
					throw new ListBuilderException("Valor '%s' não é uma data válida", item.getKey());
				}
			}

			log.info("Datas {} validadas com Sucesso!", msg);
		}
		return mapDate;
	}

	/** Convert millimeters in Points */
	public static float getPointsFromMM(int mm) {
		return mm * ONE_MM_IN_POINT;
	}
	
	/** POI Uses Metric Unit representative of 1/20 Point */
	public static float getSizePointTimesTwenty(float points) {
		return SIZE_SCALE * points;
	}
	
	/** POI Uses Metric Unit representative of 1/20 Point */
	public static int getSizePointTimesTwenty(int points) {
		return SIZE_SCALE * points;
	}

	public static float getHorizontalMargins(ListTypeEnum listType) {
		return listType.getPageMg().getLeft() + listType.getPageMg().getRight() - AppUtils.getPointsFromMM(5);
	}

	public static <T> boolean listIsNullOrEmpty(List<T> list) {
		return Objects.isNull(list) || list.isEmpty();
	}
	
	public static boolean valueIsNullOrBlank(String value) {
		return Objects.isNull(value) || value.isBlank();
	}

}
