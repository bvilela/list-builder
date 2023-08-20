package br.com.bvilela.listbuilder.builder.clearing;

import br.com.bvilela.listbuilder.dto.clearing.input.ClearingInputDTO;
import java.util.List;
import java.util.Map;

public class ClearingInputDtoBuilder {

    private static final String LAST_DATE_DEFAULT = "29-03-2022";

    private final Map<Integer, String> groupsDefault =
            Map.of(
                    1, "Group 1 (Person1, Person2 Person3)",
                    2, "Group 2 (Person3, Person4, Person5, Person6)",
                    3, "Group 3 (Person7, Person8, Person9, Person10, Person11)",
                    4, "Group 4 (Person12, Person13, Person14, Person15, Person16)",
                    5, "Group 5 (Person17, Person18, Person19, Person20, Person21)",
                    6, "Group 6 (Person22, Person23, Person24, Person25, Person26, Person27)",
                    7, "Group 7 (Person28, Person29, Person30, Person31, Person32)",
                    8, "Group 8 (Person33, Person34, Person35, Person36)");

    private final ClearingInputDTO target;

    public ClearingInputDtoBuilder() {
        this.target = new ClearingInputDTO();
    }

    public static ClearingInputDtoBuilder create() {
        return new ClearingInputDtoBuilder();
    }

    public ClearingInputDTO build() {
        return target;
    }

    public ClearingInputDtoBuilder withLastDateInvalid() {
        return baseLastDate("01-13-2022");
    }

    public ClearingInputDtoBuilder withLastGroupNull() {
        return baseLastGroup(null);
    }

    public ClearingInputDtoBuilder withLastGroupInvalid() {
        return baseLastGroup(0);
    }

    public ClearingInputDtoBuilder withGroupsNull() {
        return base(LAST_DATE_DEFAULT, 8, "terça", "sábado", null);
    }

    public ClearingInputDtoBuilder withMidweekInvalid() {
        return baseMidweekDay("tercaaa");
    }

    public ClearingInputDtoBuilder withWeekendNull() {
        return baseWeekendDay(null);
    }

    public ClearingInputDtoBuilder withWeekendEmpty() {
        return baseWeekendDay("");
    }

    public ClearingInputDtoBuilder withWeekendBlank() {
        return baseWeekendDay(" ");
    }

    public ClearingInputDtoBuilder withWeekendInvalid() {
        return baseWeekendDay("sabadooo");
    }

    public ClearingInputDtoBuilder withSuccess() {
        return base(LAST_DATE_DEFAULT, 1, "terça", "sábado", groupsDefault);
    }

    private ClearingInputDtoBuilder baseMidweekDay(String midweekDay) {
        return base(LAST_DATE_DEFAULT, 1, midweekDay, "sábado", groupsDefault);
    }

    private ClearingInputDtoBuilder baseWeekendDay(String weekendDay) {
        return base(LAST_DATE_DEFAULT, 1, "terça", weekendDay, groupsDefault);
    }

    private ClearingInputDtoBuilder baseLastDate(String lastDate) {
        return base(lastDate, 1, "terça", "sábado", groupsDefault);
    }

    private ClearingInputDtoBuilder baseLastGroup(Integer lastGroup) {
        return base(LAST_DATE_DEFAULT, lastGroup, "terça", "sábado", groupsDefault);
    }

    private ClearingInputDtoBuilder base(
            String lastDate,
            Integer lastGroup,
            String dayMidweek,
            String dayWeekend,
            Map<Integer, String> groups) {
        this.withLastDate(lastDate);
        this.withLastGroup(lastGroup);
        this.withMeetingDayMidweek(dayMidweek);
        this.withMeetingDayWeekend(dayWeekend);
        this.withGroups(groups);
        this.withHeaderMessage("Header Message");
        this.withFooterMessage("Footer Message");
        this.withRemoveFromList(null);
        this.withAddToList(null);
        return this;
    }

    public ClearingInputDtoBuilder withLastDate(String lastDate) {
        this.target.setLastDate(lastDate);
        return this;
    }

    private ClearingInputDtoBuilder withLastGroup(Integer lastGroup) {
        this.target.setLastGroup(lastGroup);
        return this;
    }

    public ClearingInputDtoBuilder withMeetingDayMidweek(String meetingDayMidweek) {
        this.target.setMeetingDayMidweek(meetingDayMidweek);
        return this;
    }

    private void withMeetingDayWeekend(String meetingDayWeekend) {
        this.target.setMeetingDayWeekend(meetingDayWeekend);
    }

    private void withGroups(Map<Integer, String> groups) {
        this.target.setGroups(groups);
    }

    private void withHeaderMessage(String headerMessage) {
        this.target.setHeaderMessage(headerMessage);
    }

    private void withFooterMessage(String footerMessage) {
        this.target.setFooterMessage(footerMessage);
    }

    public ClearingInputDtoBuilder withRemoveFromList(List<String> removeFromList) {
        this.target.setRemoveFromList(removeFromList);
        return this;
    }

    public ClearingInputDtoBuilder withAddToList(Map<String, String> addToList) {
        this.target.setAddToList(addToList);
        return this;
    }
}
