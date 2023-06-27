package br.com.bvilela.listbuilder.dto;

import br.com.bvilela.lib.utils.annotation.javax.ValidParseDate;
import br.com.bvilela.listbuilder.config.MessageConfig;
import br.com.bvilela.listbuilder.enuns.DayOfWeekEnum;
import com.google.gson.annotations.SerializedName;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public abstract class BaseFileInputDataDTO {

    @ValidParseDate(
            message =
                    "Última Data da Lista Anterior inválida: '${validatedValue}' não é uma data válida",
            pattern = "dd-MM-yyyy",
            parse = false,
            messageRequired = MessageConfig.LAST_DATE_REQUIRED)
    @SerializedName("ultimaData")
    private String lastDate;

    @NotBlank(message = MessageConfig.MSG_ERROR_MIDWEEK_DAY_NOT_FOUND)
    @SerializedName("diaReuniaoMeioSemana")
    private String meetingDayMidweek;

    @NotBlank(message = MessageConfig.MSG_ERROR_WEEKEND_DAY_NOT_FOUND)
    @SerializedName("diaReuniaoFimSemana")
    private String meetingDayWeekend;

    public DayOfWeekEnum getMeetingDayMidweekEnum() {
        return DayOfWeekEnum.getByValue(this.meetingDayMidweek);
    }

    public DayOfWeekEnum getMeetingDayWeekendEnum() {
        return DayOfWeekEnum.getByValue(this.meetingDayWeekend);
    }
}
