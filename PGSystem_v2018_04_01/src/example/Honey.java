/**
 * Example class
 *
 * @author Sebastian Hennebrueder created Jan 16, 2006 copyright 2006 by
 * http://www.laliluna.de
 */
package example;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;

@Entity
public class Honey {

    @Id
    @GeneratedValue
    private Integer id;

    private String name;

    private String taste;   

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    @Override
    public String toString() {
        return "Honey: " + getId() + " Name: " + getName() + " Taste: " + getTaste();
    }

}
