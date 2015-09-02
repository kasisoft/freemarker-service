package com.kasisoft.cdi.services.freemarker;

import freemarker.template.*;

/**
 * Directive implementation for Freemarker which will be loaded through SPI.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
public interface FreemarkerDirective {

  /**
   * Returns the name for this directive.
   * 
   * @return   The name for this directive. Neither <code>null</code> nor empty.
   */
  String getName();
  
  /**
   * Returns the {@link TemplateModel} instance used to render some content.
   * 
   * @return   The {@link TemplateModel} instance used to render some content. Not <code>null</code>.
   */
  TemplateModel getTemplateModel();
  
} /* ENDINTERFACE */
