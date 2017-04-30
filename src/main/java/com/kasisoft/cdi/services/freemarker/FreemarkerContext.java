package com.kasisoft.cdi.services.freemarker;

import com.kasisoft.libs.common.constants.*;

import lombok.experimental.*;

import lombok.*;

import freemarker.template.*;

import java.util.*;

/**
 * This data structure allows to provide all necessary information needed to run the Freemarker templating engine.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@EqualsAndHashCode(of = { "version", "templateLoader", "encoding", "sharedVariables" })
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FreemarkerContext {

  Version                       version          = Configuration.VERSION_2_3_23;
  Encoding                      encoding         = Encoding.UTF8;
  
  Locale                        locale           = Locale.ENGLISH;
  
  boolean                       angularBrackets  = true;

  @Setter(AccessLevel.PRIVATE)
  Properties                    settings         = new Properties();
  
  @Setter(AccessLevel.PRIVATE)
  CustomTemplateLoader          templateLoader   = new CustomTemplateLoader();
  
  boolean                       enableFmx        = false;
  
  @Setter(AccessLevel.PRIVATE)
  Map<String,Object>            sharedVariables  = new HashMap<>();
  
  ObjectWrapper                 objectWrapper    = null;
  
  TemplateExceptionHandler      exceptionHandler = null;
  
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
