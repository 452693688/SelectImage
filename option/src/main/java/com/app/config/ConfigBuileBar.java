package com.app.config;

/**
 * 导航条
 * Created by Administrator on 2016/10/19.
 */
public class ConfigBuileBar {
    private int statusBarColor;
    private int actionBarColor;
    private int barBackColor;
    private int barTitleColor;
    private int barOptionColor;
    //
    private int actionBarHeight;
    private int barOptionHeight;
    //
    private int barBackSize;
    private int barTitleSize;
    private int barOptionSize;
    //
    private int barBackIconId;
    private int barOptionBackdropId;
    //
    private String barBackHint;
    private String barTitleHint, barCorpTitleHint;
    private String barOptionHint;

    public ConfigBuileBar setStatusBarColor(int statusBarColor) {
        this.statusBarColor = statusBarColor;
        return this;
    }

    public ConfigBuileBar setActionBarColor(int actionBarColor) {
        this.actionBarColor = actionBarColor;
        return this;
    }

    public ConfigBuileBar setBarBackColor(int barBackColor) {
        this.barBackColor = barBackColor;
        return this;
    }

    public ConfigBuileBar setBarTitleColor(int barTitleColor) {
        this.barTitleColor = barTitleColor;
        return this;
    }

    public ConfigBuileBar setBarOptionColor(int barOptionColor) {
        this.barOptionColor = barOptionColor;
        return this;
    }

    public ConfigBuileBar setBarBackSize(int barBackSize) {
        this.barBackSize = barBackSize;
        return this;
    }

    public ConfigBuileBar setBarTitleSize(int barTitleSize) {
        this.barTitleSize = barTitleSize;
        return this;
    }

    public ConfigBuileBar setBarOptionSize(int barOptionSize) {
        this.barOptionSize = barOptionSize;
        return this;
    }

    public ConfigBuileBar setBarBackIconId(int barBackIconId) {
        this.barBackIconId = barBackIconId;
        return this;
    }

    public ConfigBuileBar setBarOptionBackdropId(int barOptionBackdropId) {
        this.barOptionBackdropId = barOptionBackdropId;
        return this;
    }

    public ConfigBuileBar setBarBackHint(String barBackHint) {
        this.barBackHint = barBackHint;
        return this;
    }

    public ConfigBuileBar setBarTitleHint(String barTitleHint) {
        this.barTitleHint = barTitleHint;
        return this;
    }
    public ConfigBuileBar setBarCorpTitleHint(String barCorpTitleHint) {
        this.barCorpTitleHint = barCorpTitleHint;
        return this;
    }

    public ConfigBuileBar setBarOptionHint(String barOptionHint) {
        this.barOptionHint = barOptionHint;
        return this;
    }

    public ConfigBuileBar setActionBarHeight(int actionBarHeight) {
        this.actionBarHeight = actionBarHeight;
        return this;
    }

    public ConfigBuileBar setBarOptionHeight(int barOptionHeight) {
        this.barOptionHeight = barOptionHeight;
        return this;
    }

    public ConfigBuile complete() {
        return ConfigBuile.build;
    }

    public int getStatusBarColor() {
        return statusBarColor;
    }

    public int getActionBarColor() {
        return actionBarColor;
    }

    public int getBarBackColor() {
        return barBackColor;
    }

    public int getBarTitleColor() {
        return barTitleColor;
    }

    public int getBarOptionColor() {
        return barOptionColor;
    }

    public int getBarBackSize() {
        return barBackSize;
    }

    public int getBarTitleSize() {
        return barTitleSize;
    }

    public int getBarOptionSize() {
        return barOptionSize;
    }

    public int getBarBackIconId() {
        return barBackIconId;
    }

    public int getBarOptionBackdropId() {
        return barOptionBackdropId;
    }

    public String getBarBackHint() {
        return barBackHint;
    }

    public String getBarTitleHint() {
        return barTitleHint;
    }
    public String getBarCorpTitleHint() {
        return barCorpTitleHint;
    }
    public String getBarOptionHint() {
        return barOptionHint;
    }

    public int getActionBarHeight() {
        return actionBarHeight;
    }

    public int getBarOptionHeight() {
        return barOptionHeight;
    }
}
