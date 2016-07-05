package fr.adaming.website.bean;

/**
 * Created by INTI-0332 on 05/07/2016.
 */
public class ErrorBean {

    private String message;

    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ErrorBean() {

    }

    public ErrorBean(String message, String code) {
        this.message = message;
        this.code = code;
    }
}
