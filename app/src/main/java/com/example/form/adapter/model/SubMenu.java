package com.example.form.adapter.model;

public class SubMenu {

    String namaMenu, target, icon;

    public SubMenu(String namaMenu, String target, String icon) {
        this.namaMenu = namaMenu;
        this.target = target;
        this.icon = icon;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
