package be.atc.salesmanagercrm.beans;


import be.atc.salesmanagercrm.dao.ConversationsDao;
import be.atc.salesmanagercrm.dao.impl.ConversationsDaoImpl;
import be.atc.salesmanagercrm.entities.ConversationsEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;


/**
 * @author Larché Marie-Élise
 */
@Slf4j
@Named(value = "conversationsBean")
@SessionScoped
public class ConversationsBean implements Serializable {

    @Getter
    @Setter
    private ConversationsEntity conversationsEntity = new ConversationsEntity();

    @Getter
    @Setter
    private ConversationsDao dao = new ConversationsDaoImpl();

    public void add() {

    }


}
