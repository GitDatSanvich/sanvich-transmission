package git.datsanvich.nada.model;

/**
 * 公共错误码
 * @author zhaosongbin
 * @date 14:21
 */
public enum STATUS {

    /**
     * 操作成功
     */
    SUCCESS("0000", "操作成功"),

    /**
     * 操作失败
     */
    ERROR("0001", "操作失败"),

    /**
     * 未知错误
     */
    ERROR_UNKNOWN("0002", "未知错误"),



    /**
     * 参数为空
     */
    PARAM_NULL("0010","参数为空"),

    /**
     * 参数错误
     */
    ERROR_PARAM("0011", "参数错误"),


    /**
     * 获取结果为空
     */
    EMPTY_RESULT("0020", "获取结果为空"),

    /**
     * 获取结果错误
     */
    ERROR_RESULT("0021", "获取结果错误"),


    /**
     * 退出失败
     */
    LOGOUT_ERROR("0030","退出失败"),


    /**
     * JSON格式错误
     */
    ERROR_JSON("0040", "JSON格式错误"),


    /**
     * 401无效的访问令牌字符串
     */
    INVALID_TOKEN("0401","token为空");



    String value;
    String name;

    STATUS(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static String getName(String value) {
        for (STATUS category : STATUS.values()) {
            if (category.getValue() == value) {
                return category.getName();
            }
        }
        return null;
    }

    public static STATUS value(String value) {
        for (STATUS category : STATUS.values()) {
            if (category.getValue() == value) {
                return category;
            }
        }
        return null;
    }
}
