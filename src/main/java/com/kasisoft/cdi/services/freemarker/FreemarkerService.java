package com.kasisoft.cdi.services.freemarker;

import javax.ejb.*;
import javax.ejb.Singleton;
import javax.inject.*;

/**
 * Provide the basic service functionality providing it as a bean.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Named @Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class FreemarkerService extends AbstractFreemarkerService {
} /* ENDCLASS */
