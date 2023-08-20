package br.com.bvilela.listbuilder.config;

import br.com.bvilela.listbuilder.util.AppUtils;

public final class SizeConfig {

    private SizeConfig() {}

    // PageSize A4 (595 x 842 Points)
    public static final int DOCX_A4_WIDTH_POINT = AppUtils.getSizePointTimesTwenty(595);
    public static final int DOCX_A4_HEIGHT_POINT = AppUtils.getSizePointTimesTwenty(842);

    // Margin Sizes
    public static final MarginBase DEFAULT_MARGIN = new MarginBase(10, 10, 10, 10);
    public static final MarginBase AUDIENCE_MARGIN = new MarginBase(25, 25, 9, 9);
    public static final MarginBase CHRISTIAN_LIFE_MARGIN = new MarginBase(10, 10, 7, 10);

    // Header Sizes
    public static final SizeBase DEFAULT_HEADER = new SizeBase(190, 19);
    public static final SizeBase AUDIENCE_HEADER = new SizeBase(180, 22);
    public static final SizeBase CHRISTIAN_LIFE_HEADER = new SizeBase(198, 18);

    // SubHeader Sizes
    public static final SizeBase DEFAULT_SUBHEADER = new SizeBase(90, 10);
    public static final SizeBase CHRISTIAN_LIFE_SUBHEADER = new SizeBase(95, 9);
}
