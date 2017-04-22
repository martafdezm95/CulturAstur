
package es.uniovi.imovil.user.courses.Models;

import java.io.Serializable;

import es.uniovi.imovil.user.courses.Models.DynamicContent__;

public class DynamicElement implements Serializable {

    private DynamicContent__ dynamicContent;
    private String name;

    public DynamicContent__ getDynamicContent() {
        return dynamicContent;
    }

    public void setDynamicContent(DynamicContent__ dynamicContent) {
        this.dynamicContent = dynamicContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
