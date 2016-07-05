package fr.adaming.website.bean;

/**
 * Created by INTI-0332 on 05/07/2016.
 */
public class CityBean {

    private String ville;

    private String cp;

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public CityBean() {
    }

    public CityBean(String cp, String ville) {
        this.cp = cp;
        this.ville = ville;
    }

    @Override
    public String toString() {
        return "CityBean{" +
                "ville='" + ville + '\'' +
                ", cp='" + cp + '\'' +
                '}';
    }
}
