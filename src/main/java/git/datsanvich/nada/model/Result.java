package git.datsanvich.nada.model;

import java.io.Serializable;

/**
 * 和前端交互的数据结果
 * @author zhaosongbin
 * @date 2019/3/25 15:59
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 5870221908873132839L;
    /**
     * 返回的状态码
     */
    private String code;

    /**
     * 返回给页面的消息
     */
    private String msg;

    /**
     * 返回的内容
     */
    private T data;

    private String process;


    public Result() {
        super();
    }


    public Result(String code, String msg, T data, String process) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
        this.process = process;
    }

    /**
     *
     * @param data data
     * @param <T> <T>
     * @return 返回成功
     */

    public static <T> Result<T> success(T data,String code, String msg,String process) {
        Result<T> resultData = new Result<T>();
        resultData.setCode(code);
        resultData.setMsg(msg);
        resultData.setData(data);
        resultData.setProcess(process);
        return resultData;
    }

    /**
     *
     * @param code code
     * @param msg msg
     * @param <T> <T>
     * @return 返回失败
     */
    public static <T> Result<T> error(String code, String msg,String process) {
        Result<T> resultData = new Result<T>();
        resultData.setCode(code);
        resultData.setMsg(msg);
        resultData.setProcess(process);
        return resultData;
    }

    @Override
    public String toString()
    {

        return "{\"code\":\"" + code + "\",\"msg\":\""
                + msg + "\",\"data\":\"" + data + "\",\"process\":\"" + process + "\"}";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
