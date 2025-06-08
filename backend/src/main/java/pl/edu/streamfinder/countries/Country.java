package pl.edu.streamfinder.countries;

import lombok.Getter;

@Getter
public enum Country {
    US("United States"),
    CA("Canada"),
    AU("Australia"),
    DE("Germany"),
    FR("France"),
    IT("Italy"),
    ES("Spain"),
    JP("Japan"),
    BR("Brazil"),
    MX("Mexico"),
    NL("Netherlands"),
    SE("Sweden"),
    NO("Norway"),
    DK("Denmark"),
    FI("Finland"),
    RU("Russia"),
    KR("South Korea"),
    CN("China"),
    TW("Taiwan"),
    HK("Hong Kong"),
    SG("Singapore"),
    ID("Indonesia"),
    TH("Thailand"),
    PH("Philippines"),
    MY("Malaysia"),
    VN("Vietnam"),
    PL("Poland"),
    CZ("Czech Republic"),
    HU("Hungary"),
    SK("Slovakia"),
    AT("Austria"),
    CH("Switzerland"),
    BE("Belgium"),
    PT("Portugal"),
    IE("Ireland"),
    GR("Greece"),
    TR("Turkey"),
    AR("Argentina"),
    CL("Chile"),
    CO("Colombia"),
    PE("Peru"),
    VE("Venezuela"),
    EC("Ecuador"),
    BO("Bolivia"),
    UY("Uruguay"),
    PY("Paraguay"),
    PA("Panama"),
    CR("Costa Rica"),
    GT("Guatemala"),
    SV("El Salvador"),
    HN("Honduras"),
    NI("Nicaragua"),
    RS("Serbia"),
    IN("India"),
    UA("Ukraine"),
    AZ("Azerbaijan"),
    LT("Lithuania"),
    MK("North Macedonia"),
    AE("United Arab Emirates"),
    IL("Israel"),
    MD("Moldova"),
    ZA("South Africa"),
    BG("Bulgaria"),
    SI("Slovenia"),
    CY("Cyprus"),
    NZ("New Zealand"),
    HR("Croatia"),
    EE("Estonia"),
    IS("Iceland"),
    RO("Romania"),
    GB("United Kingdom");

    private final String fullName;

    Country(String fullName) {
        this.fullName = fullName;
    }

    public String getCode() {
        return name().toLowerCase();
    }

}
