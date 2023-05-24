package br.com.bvilela.listbuilder.config;

import br.com.bvilela.listbuilder.utils.AppUtils;
import lombok.Getter;

@Getter
public class MarginBase {
    private float left;
    private float right;
    private float top;
    private float bottom;

    /** Size in millimeters */
    public MarginBase(int left, int right, int top, int bottom) {
        this.left = AppUtils.getPointsFromMM(left);
        this.right = AppUtils.getPointsFromMM(right);
        this.top = AppUtils.getPointsFromMM(top);
        this.bottom = AppUtils.getPointsFromMM(bottom);
    }
}
