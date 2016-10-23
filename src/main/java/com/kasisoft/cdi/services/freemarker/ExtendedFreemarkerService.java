package com.kasisoft.cdi.services.freemarker;

import com.kasisoft.libs.common.util.*;

import com.kasisoft.libs.common.spi.*;

import lombok.experimental.*;

import lombok.*;

import freemarker.template.*;

import javax.inject.*;

import javax.ejb.*;
import javax.ejb.Singleton;

import javax.annotation.*;

import java.util.*;

import java.io.*;

/**
 * This is a an extension of the {@link FreemarkerService} which provides a more structured way of rendering. 
 * Therefore the templates will be invoked with the following settings:
 * 
 * - the model is provided through a property 'model' to the template
 * - all directives registered through the interface {@link FreemarkerDirective} can be loaded
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@Named @Singleton
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class ExtendedFreemarkerService extends AbstractFreemarkerService {

  Bucket<HashMap<String,Object>>   bucket     = new Bucket<>( BucketFactories.newHashMapFactory() );

  List<FreemarkerDirective>        directives = Collections.emptyList();
  
  @PostConstruct
  public void postConstruct() {
    SPILoader<FreemarkerDirective> loader = SPILoader.<FreemarkerDirective>builder()
      .serviceType( FreemarkerDirective.class )
      .build();
    directives  = loader.loadServices();
  }
  
  public void configure( @NonNull Map<String,Object> configuration ) {
    directives.stream()
      .filter( $ -> $ instanceof Configurable )
      .forEach( $ -> ((Configurable) $).configure( configuration ) );
  }
  
  @Override
  protected Configuration newConfiguration( FreemarkerContext descriptor ) throws TemplateException {
    Configuration result = super.newConfiguration( descriptor );
    directives.forEach( $ -> result.setSharedVariable( $.getName(), $.getTemplateModel() ) );
    return result;
  }
  
  @Override
  public void generate( @NonNull FreemarkerContext descriptor, @NonNull String template, @NonNull Writer writer ) {
    generate( descriptor, template, writer, (TemplateModel) null, null );
  }

  public void generate( @NonNull FreemarkerContext descriptor, @NonNull String template, @NonNull Writer writer, @NonNull Object modelobj ) {
    generate( descriptor, template, writer, modelobj, null );
  }

  @Override
  public void generate( @NonNull FreemarkerContext descriptor, @NonNull String template, @NonNull Writer writer, @NonNull Map<String,Object> params ) {
    generate( descriptor, template, writer, params, null );
  }

  @Override
  public void generate( @NonNull FreemarkerContext descriptor, @NonNull String template, @NonNull Writer writer, @NonNull TemplateModel model ) {
    generate( descriptor, template, writer, model, null );
  }

  public void generate( @NonNull FreemarkerContext descriptor, @NonNull String name, @NonNull Writer writer, Object modelobj, Locale locale ) {
    generate( descriptor, name, writer, modelobj, null, locale );
  }
  
  @Override
  public void generate( @NonNull FreemarkerContext descriptor, @NonNull String name, @NonNull Writer writer, Map<String,Object> params, Locale locale ) {
    generate( descriptor, name, writer, params, null, locale );
  }

  public void generate( @NonNull FreemarkerContext descriptor, @NonNull String name, @NonNull Writer writer, Object modelobj, Map<String, Object> additional, Locale locale ) {
    bucket.forInstanceDo( $ -> super.generate( descriptor, name, writer, asModel( $, modelobj, additional ), locale ) );
  }
  
  public void generate( @NonNull FreemarkerContext descriptor, @NonNull String name, @NonNull Writer writer, Map<String,Object> params, Map<String, Object> additional, Locale locale ) {
    bucket.forInstanceDo( $ -> super.generate( descriptor, name, writer, asModel( $, params, additional ), locale ) );
  }

  public void generate( @NonNull FreemarkerContext descriptor, @NonNull String name, @NonNull Writer writer, TemplateModel model, Map<String, Object> additional, Locale locale ) {
    bucket.forInstanceDo( $ -> super.generate( descriptor, name, writer, asModel( $, model, additional ), locale ) );
  }

  private <T> Map<String,Object> asModel( HashMap<String,Object> map, T model, Map<String, Object> additional ) {
    if( additional != null ) {
      map.putAll( additional );
    }
    map.put( FreemarkerKeys.FMK_MODEL, model );
    return map;
  }
  
} /* ENDCLASS */
