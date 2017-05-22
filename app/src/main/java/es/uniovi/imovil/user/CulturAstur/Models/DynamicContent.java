
package es.uniovi.imovil.user.CulturAstur.Models;

import java.io.Serializable;

public class DynamicContent implements Serializable {

    private String content;
    private String name;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
