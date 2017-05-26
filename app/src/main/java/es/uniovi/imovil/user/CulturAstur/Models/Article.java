
package es.uniovi.imovil.user.CulturAstur.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Article implements Serializable {
    public List<DynamicElement> dynamicElement = null;
    private double lat;
    private double lng;
    private String URL = null;
    private String name = null;
    private String council = null;
    private String zone = null;
    private String direction = null;
    private List<String> info = new ArrayList<String>();
    private String email = null;
    private String web = null;
    private String telephone = null;
    private double dist;
    private int fav = -1; //Posicion en lista de favoritos
    private int modelPos = -1; //posicion en lista principal

    public int isFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public int getModelPos() {
        return modelPos;
    }

    public void setModelPos(int modelPos) {
        this.modelPos = modelPos;
    }

    public double getDist() {
        return dist;
    }

    public void setDist(double dist) {
        this.dist = dist;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getURL() {
        return URL;
    }


    public void setURL(String URL) {
        this.URL = URL;
    }

    public List<DynamicElement> getDynamicElement() {
        return dynamicElement;
    }

    public void setDynamicElement(List<DynamicElement> dynamicElement) {
        this.dynamicElement = dynamicElement;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCouncil() {
        return council;
    }

    public void setCouncil(String council) {
        this.council = council;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public List<String> getInfo() {
        return info;
    }

    public void setInfo(List<String> info) {
        this.info = info;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
