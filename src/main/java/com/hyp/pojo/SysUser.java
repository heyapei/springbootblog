package com.hyp.pojo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "sys_user")
public class SysUser {
    @Id
    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private Integer age;

    private Integer sex;

    private Integer job;

    @Column(name = "faceImage")
    private String faceimage;

    private String province;

    private String city;

    private String district;

    private String address;

    @Column(name = "authSalt")
    private String authsalt;

    @Column(name = "lastLoginIp")
    private String lastloginip;

    @Column(name = "lastLoginTime")
    private Date lastlogintime;

    @Column(name = "isDelete")
    private String isdelete;

    @Column(name = "registTime")
    private Date registtime;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return age
     */
    public Integer getAge() {
        return age;
    }

    /**
     * @param age
     */
    public void setAge(Integer age) {
        this.age = age;
    }

    /**
     * @return sex
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * @param sex
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * @return job
     */
    public Integer getJob() {
        return job;
    }

    /**
     * @param job
     */
    public void setJob(Integer job) {
        this.job = job;
    }

    /**
     * @return faceImage
     */
    public String getFaceimage() {
        return faceimage;
    }

    /**
     * @param faceimage
     */
    public void setFaceimage(String faceimage) {
        this.faceimage = faceimage;
    }

    /**
     * @return province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return district
     */
    public String getDistrict() {
        return district;
    }

    /**
     * @param district
     */
    public void setDistrict(String district) {
        this.district = district;
    }

    /**
     * @return address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return authSalt
     */
    public String getAuthsalt() {
        return authsalt;
    }

    /**
     * @param authsalt
     */
    public void setAuthsalt(String authsalt) {
        this.authsalt = authsalt;
    }

    /**
     * @return lastLoginIp
     */
    public String getLastloginip() {
        return lastloginip;
    }

    /**
     * @param lastloginip
     */
    public void setLastloginip(String lastloginip) {
        this.lastloginip = lastloginip;
    }

    /**
     * @return lastLoginTime
     */
    public Date getLastlogintime() {
        return lastlogintime;
    }

    /**
     * @param lastlogintime
     */
    public void setLastlogintime(Date lastlogintime) {
        this.lastlogintime = lastlogintime;
    }

    /**
     * @return isDelete
     */
    public String getIsdelete() {
        return isdelete;
    }

    /**
     * @param isdelete
     */
    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }

    /**
     * @return registTime
     */
    public Date getRegisttime() {
        return registtime;
    }

    /**
     * @param registtime
     */
    public void setRegisttime(Date registtime) {
        this.registtime = registtime;
    }
}