
package es.uniovi.imovil.user.courses.Models;

import java.io.Serializable;
import java.util.List;

public class Article implements Serializable {
    public List<DynamicElement> dynamicElement = null;

    public List<DynamicElement> getDynamicElement() {
        return dynamicElement;
    }

    public void setDynamicElement(List<DynamicElement> dynamicElement) {
        this.dynamicElement = dynamicElement;
    }
}
