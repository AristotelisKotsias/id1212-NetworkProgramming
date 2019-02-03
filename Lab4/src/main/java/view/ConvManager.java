package view;

import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Named;
import controller.Facade;

@Named("convManager")
@ConversationScoped
public class ConvManager implements Serializable {

    @EJB
    private Facade fasade;
    private String fromCurr;
    private String toCurr;
    private double amount;
    private String converted;

    public String getConverted() {
        return converted;
    }

    public void convertCurrency() {
        converted = fasade.converter(fromCurr, toCurr, amount);
    }

    public String getFromCurr() {
        return fromCurr;
    }

    public void setFromCurr(String fromCurr) {
        this.fromCurr = fromCurr;
    }

    public String getToCurr() {
        return toCurr;
    }

    public void setToCurr(String toCurr) {
        this.toCurr = toCurr;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
