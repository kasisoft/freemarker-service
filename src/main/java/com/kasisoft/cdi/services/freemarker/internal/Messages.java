package com.kasisoft.cdi.services.freemarker.internal;

import com.kasisoft.libs.common.i18n.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class Messages {

  @I18N("The template '%s' could not be processed. Cause: %s")
  public static I18NFormatter   failed_to_process_template;
  
  @I18N("The template '%s' could not be loaded. Cause: %s")
  public static I18NFormatter   failed_to_load_template;
  
  @I18N("The template '%s' is either missing or contains an error.")
  public static I18NFormatter   missing_or_invalid_template;
  
  static {
    I18NSupport.initialize( Messages.class );
  }

} /* ENDCLASS */
