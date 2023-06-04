package br.com.bvilela.listbuilder.service.notification;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NotifyUtils {

    public static boolean containsName(List<String> list, String nameNotify) {
        if (CollectionUtils.isEmpty(list)) {
            return false;
        }
        return list.stream().anyMatch(e -> e.toLowerCase().contains(nameNotify.toLowerCase()));
    }

    public static boolean containsName(String item, String nameNotify) {
        if (item == null) {
            return false;
        }
        return item.toLowerCase().contains(nameNotify.toLowerCase());
    }
}
