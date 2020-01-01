package com.hyp.pojo.datatransferobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2019/12/1 12:42
 * @Description: TODO 添加注解忽略没有属性值
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDTO {
    private String date;

    private String ymd;

    private String high;

    private String sunrise;

    private String fx;

    private String week;

    private String low;

    private String fl;

    private String sunset;

    private String type;

    private String notice;

    @Override
    public String toString() {
        return "WeatherDTO{" +
                "date='" + date + '\'' +
                ", ymd='" + ymd + '\'' +
                ", high='" + high + '\'' +
                ", sunrise='" + sunrise + '\'' +
                ", fx='" + fx + '\'' +
                ", week='" + week + '\'' +
                ", low='" + low + '\'' +
                ", fl='" + fl + '\'' +
                ", sunset='" + sunset + '\'' +
                ", type='" + type + '\'' +
                ", notice='" + notice + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getYmd() {
        return ymd;
    }

    public void setYmd(String ymd) {
        this.ymd = ymd;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getFx() {
        return fx;
    }

    public void setFx(String fx) {
        this.fx = fx;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getFl() {
        return fl;
    }

    public void setFl(String fl) {
        this.fl = fl;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }
}
