
package es.uniovi.imovil.user.CulturAstur.Models;

import java.io.Serializable;

public class DynamicElement implements Serializable {

    private DynamicContent dynamicContent;
    private String name;

    public DynamicContent getDynamicContent() {
        return dynamicContent;
    }

    public void setDynamicContent(DynamicContent dynamicContent) {
        this.dynamicContent = dynamicContent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
