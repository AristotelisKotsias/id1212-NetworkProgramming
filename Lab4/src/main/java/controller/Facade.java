package controller;

import integration.ConverterDAO;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import model.Currency;

@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Stateless
public class Facade {

    @EJB
    ConverterDAO dao;

    public String converter(String fromCurr, String toCurr, Double amount) {
      Currency currency = dao.converter(fromCurr, toCurr, amount);
      return currency.convert(toCurr, amount);
    }
}
