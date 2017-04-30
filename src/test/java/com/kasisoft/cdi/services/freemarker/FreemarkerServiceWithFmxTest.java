package com.kasisoft.cdi.services.freemarker;

import com.kasisoft.cdi.weldex.*;

import org.testng.annotations.*;

import javax.inject.*;

import lombok.experimental.*;

import lombok.*;

import freemarker.cache.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
/* @ManagedBean */ @Singleton
public class FreemarkerServiceWithFmxTest extends AbstractFreemarkerServiceTest {

  @BeforeSuite
  public void prepare() {
    freemarkerService = CdiContext.component( FreemarkerService.class );
    descriptor        = new FreemarkerContext();
    descriptor.setEnableFmx( true );
    descriptor.getTemplateLoader().add( new ClassTemplateLoader( FreemarkerServiceWithFmxTest.class, "/templates" ) );
  }
  
} /* ENDCLASS */
