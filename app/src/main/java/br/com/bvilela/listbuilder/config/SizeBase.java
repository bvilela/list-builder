package br.com.bvilela.listbuilder.config;

import br.com.bvilela.listbuilder.utils.AppUtils;
import lombok.Getter;

@Getter
public class SizeBase {
    private float width;
    private float height;

    /** Size in millimeters */
    public SizeBase(int width, int height) {
        this.width = AppUtils.getPointsFromMM(width);
        this.height = AppUtils.getPointsFromMM(height);
    }
}
