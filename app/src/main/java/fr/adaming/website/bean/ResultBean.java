package fr.adaming.website.bean;

/**
 * Created by INTI-0332 on 05/07/2016.
 */
public class ResultBean {

    private CityBean[] results;
    private int nbr;
    private ErrorBean errors;

    public CityBean[] getResults() {
        return results;
    }

    public void setResults(CityBean[] results) {
        this.results = results;
    }

    public int getNbr() {
        return nbr;
    }

    public void setNbr(int nbr) {
        this.nbr = nbr;
    }

    public ErrorBean getErrors() {
        return errors;
    }

    public void setErrors(ErrorBean errors) {
        this.errors = errors;
    }


}
