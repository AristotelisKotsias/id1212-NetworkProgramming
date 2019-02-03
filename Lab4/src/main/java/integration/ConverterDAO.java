package integration;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import model.Currency;

@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Stateless
public class ConverterDAO {

    @PersistenceContext(unitName = "currencyPU")
    private EntityManager em;

    public Currency converter(String fromCurr, String toCurr, Double amount) {

        Currency currency = em.find(Currency.class, fromCurr);
        if (currency == null) {
            throw new EntityNotFoundException("Currency does not exist " + fromCurr);
        }
        
        return currency;
    }
}
