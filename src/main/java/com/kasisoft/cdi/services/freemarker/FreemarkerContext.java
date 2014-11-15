package com.kasisoft.cdi.services.freemarker;

import com.kasisoft.libs.common.constants.*;

import freemarker.template.*;

import java.util.*;

import lombok.*;

/**
 * This data structure allows to provide all necessary information needed to run the Freemarker templating engine.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@EqualsAndHashCode(of = { "version", "templateLoader", "encoding", "sharedVariables" })
@Data
public class FreemarkerContext {

  private Version                 version         = Configuration.VERSION_2_3_21;
  private Encoding                encoding        = Encoding.UTF8;
  
  private Locale                  locale          = Locale.ENGLISH;

  @Setter(AccessLevel.PRIVATE)
  private Properties              settings        = new Properties();
  
  @Setter(AccessLevel.PRIVATE)
  private CustomTemplateLoader    templateLoader  = new CustomTemplateLoader();
  
  @Setter(AccessLevel.PRIVATE)
  private Map<String,Object>      sharedVariables = new HashMap<>();
  
  private ObjectWrapper           objectWrapper   = null;
  
  public void setEncoding( @NonNull Encoding newencoding ) {
    encoding = newencoding;
  }

  public void setVersion( @NonNull Version newversion ) {
    version = newversion;
  }

  public void setLocale( @NonNull Locale newlocale ) {
    locale = newlocale;
  }
  
  public ObjectWrapper getObjectWrapper() {
    if( objectWrapper == null ) {
      objectWrapper = new DefaultObjectWrapper( version );
    }
    return objectWrapper;
  }
  
} /* ENDCLASS */
