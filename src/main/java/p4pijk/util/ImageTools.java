package p4pijk.util;

public enum ImageTools {

    FIRST_IMAGE("1.bmp"),
    SECOND_IMAGE("2.bmp"),
    THIRD_IMAGE("3.bmp"),
    FOURTH_IMAGE("4.bmp"),
    FIRST_MATRIX(FIRST_IMAGE.value() + " matrix"),
    FIRST_BINARY_MATRIX(FIRST_IMAGE.value() + " binary matrix"),
    FIRST_GEOMETRIC_VECTOR(FIRST_IMAGE.value() + " geometric vector"),
    FIRST_SK(FIRST_IMAGE.value() + " sk"),
    FIRST_SK_PARA(FIRST_IMAGE.value() + " sk_para"),
    SECOND_MATRIX(SECOND_IMAGE.value() + " matrix"),
    SECOND_BINARY_MATRIX(SECOND_IMAGE.value() + " binary matrix"),
    SECOND_GEOMETRIC_VECTOR(SECOND_IMAGE.value() + " geometric vector"),
    SECOND_SK(SECOND_IMAGE.value() + " sk"),
    SECOND_SK_PARA(SECOND_IMAGE.value() + " sk_para"),
    BOTH_SK("Combined sk_para"),
    BOTH_SK_PARA("Combined sk_para"),
    SHENON_CRITERIA("Shenon"),
    KULBAK_CRITERIA("Kulbak"),
    OPTIMAL_DELTA("Delta"),
    LAB2_PATH("src\\main\\java\\p4pijk\\lab2\\"),
    LAB3_PATH("src\\main\\java\\p4pijk\\lab3\\"),
    LAB4_PATH("src\\main\\java\\p4pijk\\lab4\\"),
    LAB5_PATH("src\\main\\java\\p4pijk\\lab5\\"),
    LAB6_PATH("src\\main\\java\\p4pijk\\lab6\\"),
    LAB7_PATH("src\\main\\java\\p4pijk\\lab7\\"),
    RESULT_FILE("result.txt");

    private final String name;

    ImageTools(String name) {
        this.name = name;
    }

    public String value() {
        return name;
    }
}
