package com.bruno.listbuilder.service.vidacrista.impl;

import static com.bruno.listbuilder.enuns.VidaCristaExtractItemType.LABEL;
import static com.bruno.listbuilder.enuns.VidaCristaExtractItemType.NO_PARTICIPANTS;
import static com.bruno.listbuilder.enuns.VidaCristaExtractItemType.PRESIDENT;
import static com.bruno.listbuilder.enuns.VidaCristaExtractItemType.READ_OF_WEEK;
import static com.bruno.listbuilder.enuns.VidaCristaExtractItemType.WITH_PARTICIPANTS;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.bruno.listbuilder.dto.vidacrista.VidaCristaExtractWeekDTO;
import com.bruno.listbuilder.dto.vidacrista.VidaCristaExtractWeekItemDTO;
import com.bruno.listbuilder.enuns.VidaCristaExtractItemType;
import com.bruno.listbuilder.exception.ListBuilderException;
import com.bruno.listbuilder.service.vidacrista.VidaCristaExtractService;
import com.bruno.listbuilder.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VidaCristaExtractServiceImpl implements VidaCristaExtractService {
	
	private static final String STRONG = "strong";

	@Override
	public String getUrlMeetingWorkbook(LocalDate lastDate) {
		var nextMonday = DateUtils.nextDayOfWeek(lastDate, DayOfWeek.MONDAY);

		String month1 = null;
		String month2 = null;
		String year = String.valueOf(nextMonday.getYear());

		if (nextMonday.getMonthValue() % 2 == 0) {
			month1 = DateUtils.getNameMonthFull(nextMonday.plusMonths(-1));
			month2 = DateUtils.getNameMonthFull(nextMonday);
		} else {
			month1 = DateUtils.getNameMonthFull(nextMonday);
			month2 = DateUtils.getNameMonthFull(nextMonday.plusMonths(1));
		}

		return String.format("https://www.jw.org/pt/biblioteca/jw-apostila-do-mes/%s-%s-%s-mwb/", month1, month2, year);
	}

	@Override
	public List<VidaCristaExtractWeekDTO> extractWeeksBySite(String url) throws IOException, ListBuilderException {
		log.info("Sanitizando URL");
		url = sanitizeUrl(url);
		log.info("Extraindo Dados da URL: {}", url);
		Document doc = Jsoup.connect(url).get();

		Elements header = doc.select("span.contextTitle");
		checkEmpty(header, "Erro ao ler Cabeçalho do site");

		log.debug("Header: {}", header.get(0).childNodes().get(0).toString());

		List<Node> months = header.get(0).parent().childNodes();
		var monthsLabel = months.get(months.size() - 1).toString();

		log.info("Meses: {}", monthsLabel);

		Elements dates = doc.select("div.syn-body.textOnly.accordionHandle");
		checkEmpty(dates, "Erro ao ler Datas das Semanas");

		var list = new ArrayList<VidaCristaExtractWeekDTO>();
		for (Element e : dates) {
			Elements anchor = e.select("a");
			checkEmpty(anchor, "Erro ao ler Datas - Link");
			var link = anchor.attr("href");
			var labelDate = sanitizerText(anchor.html());

			log.debug("Data: {}", labelDate);
			var year = extractYearFromLabelDate(monthsLabel);
			var listDate = extractMonthsAndConvertToDates(labelDate, year);

			var dto = VidaCristaExtractWeekDTO.builder().link(link).labelDate(labelDate).date1(listDate.get(0))
					.date2(listDate.get(1)).build();
			list.add(dto);
		}

		return list;
	}

	private String sanitizeUrl(String url) {
		return url.replace("março", "marco");
	}

	private void checkEmpty(Elements elements, String msg) throws ListBuilderException {
		if (CollectionUtils.isEmpty(elements)) {
			throw new ListBuilderException(msg);
		}
	}

	public String sanitizerText(String text) {
		return text.replace("&nbsp;", " ").replace("“", "\"").replace("”", "\"").replace(" . . . ", "...")
				.replace("‘", "'").replace("’", "'").replace("</em>", "").replace("<em>", "");
	}
	
	public String adjustQuotes(String text) {
		String quotes = "\"";
		text = text.replace("\" ", quotes);
		boolean adjust = (text.startsWith(quotes) && !text.endsWith(quotes)); 
		return adjust ? text.concat(quotes) : text;
	}

	public int extractYearFromLabelDate(String label) {
		var labelSplitted = label.split(" ");
		var year = labelSplitted[labelSplitted.length - 1];
		return Integer.valueOf(year);
	}

	public List<LocalDate> extractMonthsAndConvertToDates(String label, int year) throws ListBuilderException {
		var splitted = label.split(" de ");
		
		// Ex: 21-27 de novembro
		if (splitted.length == 2) {
			var days = splitted[0];
			var month = splitted[1];
			var day1 = StringUtils.leftPad(days.split("-")[0], 2, "0");
			var day2 = StringUtils.leftPad(days.split("-")[1], 2, "0");
			var ordinalMonth = String.valueOf(DateUtils.getMonthOrdinalByNamePT(month));
			ordinalMonth = StringUtils.leftPad(ordinalMonth, 2, "0");
			var date1 = formatDate(day1, ordinalMonth, year);
			var date2 = formatDate(day2, ordinalMonth, year);

			return List.of(DateUtils.parse(date1), DateUtils.parse(date2));
		}

		//Ex: 28 de novembro–4 de dezembro
		if (splitted.length == 3) {
			var splitter = splitted[1].contains("-") ? "-" : "–";
			var day1 = StringUtils.leftPad(splitted[0], 2, "0");
			var month1 = splitted[1].split(splitter)[0];
			var day2 = StringUtils.leftPad(splitted[1].split(splitter)[1], 2, "0");
			var month2 = splitted[2];
			var ordinalMonth1 = String.valueOf(DateUtils.getMonthOrdinalByNamePT(month1));
			var ordinalMonth2 = String.valueOf(DateUtils.getMonthOrdinalByNamePT(month2));
			ordinalMonth1 = StringUtils.leftPad(ordinalMonth1, 2, "0");
			ordinalMonth2 = StringUtils.leftPad(ordinalMonth2, 2, "0");
			var date1 = formatDate(day1, ordinalMonth1, year);
			var date2 = formatDate(day2, ordinalMonth2, year);

			return List.of(DateUtils.parse(date1), DateUtils.parse(date2));
		}
		
		//Ex: 26 de dezembro de 2022–1.º de janeiro de 2023
		if (splitted.length == 5) {
			var splitter = splitted[2].contains("-") ? "-" : "–";
			var day1 = StringUtils.leftPad(splitted[0], 2, "0");
			var month1 = splitted[1];
			var year1 = splitted[2].split(splitter)[0];
			var day2Original = splitted[2].split(splitter)[1];
			var day2 = StringUtils.leftPad(day2Original.replace(".º", ""), 2, "0");
			var month2 = splitted[3];
			var year2 = splitted[4];
			var ordinalMonth1 = String.valueOf(DateUtils.getMonthOrdinalByNamePT(month1));
			var ordinalMonth2 = String.valueOf(DateUtils.getMonthOrdinalByNamePT(month2));
			ordinalMonth1 = StringUtils.leftPad(ordinalMonth1, 2, "0");
			ordinalMonth2 = StringUtils.leftPad(ordinalMonth2, 2, "0");
			var date1 = formatDate(day1, ordinalMonth1, Integer.valueOf(year1));
			var date2 = formatDate(day2, ordinalMonth2, Integer.valueOf(year2));

			return List.of(DateUtils.parse(date1), DateUtils.parse(date2));
		}

		throw new ListBuilderException("Erro ao extrair os meses da label %s", label);
	}

	private String formatDate(String day, String ordinalMonth, int year) {
		return String.format("%s-%s-%d", day, ordinalMonth, year);
	}

	@Override
	public void extractWeekItemsBySite(List<VidaCristaExtractWeekDTO> listWeeks)
			throws IOException, ListBuilderException {
		
		for (VidaCristaExtractWeekDTO week : listWeeks) {
			var list = readWeekItemsFromUrl(week.getLink());
			week.setItems(list);
		}
	}

	private List<VidaCristaExtractWeekItemDTO> readWeekItemsFromUrl(String url) throws IOException {
		var list = new ArrayList<VidaCristaExtractWeekItemDTO>();

		url = "https://www.jw.org".concat(url);
		Document doc = Jsoup.connect(url).get();
		
		addItem(list, "Presidente da Reunião", PRESIDENT);

		var textReadOfWeek = getElementStrongById(doc, "p2");
		addItem(list, textReadOfWeek, READ_OF_WEEK);

		var songAndPrayer = getElementStrongById(doc, "p3");
		addItemSongAndPrayer(list, songAndPrayer, WITH_PARTICIPANTS);

		var initialComment = getElementStrongById(doc, "p4");
		addItem(list, initialComment, NO_PARTICIPANTS);

		var treasuresLabel = doc.getElementById("p5");
		addItem(list, treasuresLabel, LABEL);

		var treasuresTitle = getElementStrongById(doc, "p6");
		addItem(list, treasuresTitle, WITH_PARTICIPANTS);

		var spiritualGems = getElementStrongById(doc, "p7");
		addItem(list, spiritualGems, WITH_PARTICIPANTS);

		var bibleReading = getElementStrongById(doc, "p10");
		if (bibleReading.isEmpty()) {
			bibleReading = getElementStrongById(doc, "p12");
		}
		addItem(list, bibleReading, WITH_PARTICIPANTS);

		var makeYourBetter = doc.getElementById("section3");
		addItem(list, makeYourBetter.select("h2"), LABEL);

		var makeYourBetterItens = makeYourBetter.select("div.pGroup").select("li");
		for (Element element : makeYourBetterItens) {
			addItem(list, element.select(STRONG), WITH_PARTICIPANTS);
		}

		var christianLife = doc.getElementById("section4");
		addItem(list, christianLife.select("h2"), LABEL);

		var christianLifeItens = christianLife.select("div.pGroup").select("li");
		for (Element element : christianLifeItens) {
			var itemStrong = element.select("strong:first-child");
			var text = sanitizerText(itemStrong.html());
			if (text.contains("Cântico")) {
				VidaCristaExtractItemType type = NO_PARTICIPANTS;
				var isPrayer = element.html().contains("e oração");
				if (isPrayer) {
					itemStrong = element.select(STRONG);
					type = WITH_PARTICIPANTS;
				}
				addItemSongAndPrayer(list, itemStrong, type);
			} else {
				var type = text.contains("Comentários finais") ? NO_PARTICIPANTS : WITH_PARTICIPANTS;
				addItem(list, itemStrong, type);
			}
		}

		return list;
	}

	private void addItemSongAndPrayer(List<VidaCristaExtractWeekItemDTO> list, Elements elements,
			VidaCristaExtractItemType type) {
		StringBuilder sb = new StringBuilder();
		for (Element e : elements) {
			sb.append(sanitizerText(e.html()));
			sb.append(" ");
		}
		sb.setLength(sb.toString().length() - 1);
		addItem(list, sb.toString(), type);
	}

	private void addItem(List<VidaCristaExtractWeekItemDTO> list, Elements elements, VidaCristaExtractItemType type) {
		StringBuilder sb = new StringBuilder();
		for (Element e : elements) {
			sb.append(sanitizerText(e.html()));
		}
		if (sb.toString().charAt(sb.toString().length() - 1) == ':') {
			sb.setLength(sb.toString().length() - 1);
		}
		addItem(list, adjustQuotes(sb.toString()), type);
	}

	private void addItem(List<VidaCristaExtractWeekItemDTO> list, Element element, VidaCristaExtractItemType type) {
		addItem(list, sanitizerText(element.html()), type);
	}

	private void addItem(List<VidaCristaExtractWeekItemDTO> list, String text, VidaCristaExtractItemType type) {
		list.add(new VidaCristaExtractWeekItemDTO(text, type));
	}

	private Elements getElementStrongById(Document doc, String id) {
		return doc.getElementById(id).select(STRONG);
	}
}
