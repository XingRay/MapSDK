package com.ray.lib_map.impl.google.params;

import android.content.Context;

import java.util.Locale;

/**
 * Author      : leixing
 * Date        : 2017-10-23
 * Email       : leixing@hecom.cn
 * Version     : 0.0.1
 * <p>
 * Description : google PlacesAPI支持的语言
 * {@see <a href="https://developers.google.com/maps/faq?hl=zh-cn#languagesupport">google language support</a>}
 */

public enum Language {
    /**
     * 简体中文
     */
    ZH_CN("zh-CN"),

    /**
     * 英语
     */
    EN("en"),

    /**
     * 阿拉伯语
     */
    AR("ar"),

    /**
     * 保加利亚语
     */
    BG("bg"),

    /**
     * 孟加拉语
     */
    BN("bn"),

    /**
     * 加泰罗尼亚语
     */
    CA("ca"),

    /**
     * 捷克语
     */
    CS("cs"),

    /**
     * 丹麦语
     */
    DA("da"),

    /**
     * 德语
     */
    DE("de"),

    /**
     * 希腊语
     */
    EL("el"),

    /**
     * 英语（澳大利亚）
     */
    EN_AU("en-AU"),

    /**
     * 英语（英国）
     */
    EN_GB("en-GB"),

    /**
     * 西班牙语
     */
    ES("es"),

    /**
     * 巴斯克语
     */
    EU("eu"),

    /**
     * 波斯语
     */
    FA("fa"),

    /**
     * 芬兰语
     */
    FI("fi"),

    /**
     * 菲律宾语
     */
    FIL("fil"),

    /**
     * 法语
     */
    FR("fr"),

    /**
     * 加利西亚语
     */
    GL("gl"),

    /**
     * 吉吉拉特语
     */
    GU("gu"),

    /**
     * 印地语
     */
    HI("hi"),

    /**
     * 克罗地亚语
     */
    HR("hr"),

    /**
     * 匈牙利语
     */
    HU("hu"),

    /**
     * 印度尼西亚语
     */
    ID("id"),

    /**
     * 意大利语
     */
    IT("it"),

    /**
     * 希伯来语
     */
    IW("iw"),

    /**
     * 日语
     */
    JA("ja"),

    /**
     * 卡纳达语
     */
    KN("kn"),

    /**
     * 韩语
     */
    KO("ko"),

    /**
     * 立陶宛语
     */
    LT("lt"),

    /**
     * 拉脱维亚语
     */
    LV("lv"),

    /**
     * 马拉雅拉姆语
     */
    ML("ml"),

    /**
     * 马拉地语
     */
    MR("mr"),

    /**
     * 荷兰语
     */
    NL("nl"),

    /**
     * 波兰语
     */
    PL("pl"),

    /**
     * 葡萄牙语
     */
    PT("pt"),

    /**
     * 葡萄牙语（巴西）
     */
    PT_BR("pt-BR"),

    /**
     * 葡萄牙语（葡萄牙）
     */
    PT_PT("pt-PT"),

    /**
     * 罗马尼亚语
     */
    RO("ro"),

    /**
     * 俄语
     */
    RU("ru"),

    /**
     * 斯洛伐克语
     */
    SK("sk"),

    /**
     * 斯洛文尼亚语
     */
    SL("sl"),

    /**
     * 塞尔维亚语
     */
    SR("sr"),

    /**
     * 瑞典语
     */
    SV("sv"),

    /**
     * 泰米尔语
     */
    TA("ta"),

    /**
     * 泰卢固语
     */
    TE("te"),

    /**
     * 泰语
     */
    TH("th"),

    /**
     * 塔加拉族语
     */
    TL("tl"),

    /**
     * 土耳其语
     */
    TR("tr"),

    /**
     * 乌克兰语
     */
    UK("uk"),

    /**
     * 越南语
     */
    VI("vi"),

    /**
     * 繁体中文
     */
    ZH_TW("zh-TW");

    private String code;

    Language(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static Language getSystemLanguage(Context context) {
        Locale locale = context.getResources().getConfiguration().locale;
        if (locale.equals(Locale.ENGLISH) || locale.equals(Locale.UK) || locale.equals(Locale.US) || locale.equals(Locale.CANADA)) {
            return EN;
        } else if (locale.equals(Locale.FRANCE) || locale.equals(Locale.FRENCH) || locale.equals(Locale.CANADA_FRENCH)) {
            return FR;
        } else if (locale.equals(Locale.GERMANY) || locale.equals(Locale.GERMAN)) {
            return DE;
        } else if (locale.equals(Locale.ITALIAN) || locale.equals(Locale.ITALY)) {
            return IT;
        } else if (locale.equals(Locale.JAPAN) || locale.equals(Locale.JAPANESE)) {
            return JA;
        } else if (locale.equals(Locale.KOREA) || locale.equals(Locale.KOREAN)) {
            return KO;
        } else if (locale.equals(Locale.CHINESE) || locale.equals(Locale.CHINA) || locale.equals(Locale.SIMPLIFIED_CHINESE) || locale.equals(Locale.PRC)) {
            return ZH_CN;
        } else if (locale.equals(Locale.TRADITIONAL_CHINESE) || locale.equals(Locale.TAIWAN)) {
            return ZH_TW;
        } else {
            return ZH_CN;
        }
    }
}
