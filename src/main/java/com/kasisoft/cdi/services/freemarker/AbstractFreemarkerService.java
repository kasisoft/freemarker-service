package com.kasisoft.cdi.services.freemarker;

import static com.kasisoft.cdi.services.freemarker.internal.Messages.*;

import com.kasisoft.libs.fmx.*;

import java.util.*;

import java.io.*;

import java.lang.ref.*;

import lombok.extern.slf4j.*;

import lombok.experimental.*;

import lombok.*;

import freemarker.cache.*;
import freemarker.template.*;
import freemarker.template.utility.*;

/**
 * This basic service basically allows to generate data of any kind with the help of the Freemarker templating 
 * engine. Although this service is supposed to be used in a CDI context it can be used as a separate utility 
 * as well.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class AbstractFreemarkerService {

  Map<FreemarkerContext, SoftReference<Configuration>>   configurations = new HashMap<>();
  
  /**
   * Creates a new configuration for the supplied descriptor.
   * 
   * @param descriptor   The descriptor providing the necessary infos to setup the configuration. Not <code>null</code>.
   * 
   * @return   The Freemarker configuration. Not <code>null</code>.
   * 
   * @throws TemplateException   The setup of the configuration failed for some reason.
   */
  protected Configuration newConfiguration( FreemarkerContext descriptor ) throws TemplateException {
    Configuration result = new Configuration( descriptor.getVersion() );
    if( descriptor.getTemplateLoader() != null ) {
      result.setTemplateLoader( descriptor.getTemplateLoader() );
    }
    if( descriptor.isEnableFmx() ) {
      result.setTemplateLoader( new MultiTemplateLoader( new TemplateLoader[] {
        new FmxTemplateLoader( descriptor.getTemplateLoader() ),
        descriptor.getTemplateLoader()
      } ) );
    }
    result.setObjectWrapper   ( descriptor.getObjectWrapper          () );
    result.setDefaultEncoding ( descriptor.getEncoding().getEncoding () );
    result.setSharedVaribles  ( descriptor.getSharedVariables        () );
    result.setSettings        ( descriptor.getSettings               () );
    result.setLocale          ( descriptor.getLocale                 () );
    if( descriptor.getExceptionHandler() !=  null ) {
        result.setTemplateExceptionHandler( descriptor.getExceptionHandler() );
    } else {
        result.setTemplateExceptionHandler( TemplateExceptionHandler.RETHROW_HANDLER );
    }
    if( descriptor.isAngularBrackets() ) {
      result.setTagSyntax( Configuration.ANGLE_BRACKET_TAG_SYNTAX );
    } else {
      result.setTagSyntax( Configuration.SQUARE_BRACKET_TAG_SYNTAX );
    }
    return result;
  }
  
  /**
   * Returns the Freemarker configuration associated with the supplied descriptor.
   * 
   * @param descriptor   The descriptor providing the necessary infos to setup the configuration. Not <code>null</code>.
   * 
   * @return   The Freemarker configuration. Not <code>null</code>.
   * 
   * @throws TemplateException   The setup of the configuration failed for some reason.
   */
  private Configuration getConfiguration( FreemarkerContext descriptor ) throws TemplateException{
    synchronized( configurations ) {
      SoftReference<Configuration> ref    = configurations.get( descriptor );
      Configuration                result = ref != null ? ref.get() : null;
      if( result == null ) {
        result = newConfiguration( descriptor );
        configurations.put( descriptor, new SoftReference<>( result ) );
      }
      return result;
    }
  }

  /**
   * Delivers the template associated with the supplied generation descriptor.
   * 
   * @param descriptor   The descriptor used to access the templates and some add ons. Not <code>null</code>.
   * @param template     The template that is supposed to be delivered. Neither <code>null</code> nor empty.
   * @param locale       The locale which will be used to locate the template. Not <code>null</code>.
   * 
   * @return   The desired Freemarker template. Not <code>null</code>.
   */
  private Template getTemplate( FreemarkerContext descriptor, String template, Locale locale ) {
    Template result = null;
    try {
      result = getConfiguration( descriptor ).getTemplate( template, locale );
      if( result == null ) {
        throw newException( true, missing_or_invalid_template.format( template ) );
      }
    } catch( FileNotFoundException ex ) {
      throw newException( true, missing_or_invalid_template.format( template ) );
    } catch( Exception ex ) {
      throw newException( ex, failed_to_load_template.format( template, ex.getLocalizedMessage() ) );
    }
    return result;  
  }
  
  /**
   * Invokes the generation process of a template.
   * 
   * @param descriptor   The descriptor used to access the templates and some add ons. Not <code>null</code>.
   * @param name         The name of the template that is supposed to be rendered. Neither <code>null</code> nor empty.
   * @param writer       The writer which is used to receive the generated content. Not <code>null</code>.
   * 
   * @throws FreemarkerException   In case of any error.
   */
  public void generate( @NonNull FreemarkerContext descriptor, @NonNull String template, @NonNull Writer writer ) {
    generate( descriptor, template, writer, (TemplateModel) null, null );
  }

  /**
   * Invokes the generation process of a template.
   * 
   * @param descriptor   The descriptor used to access the templates and some add ons. Not <code>null</code>.
   * @param name         The name of the template that is supposed to be rendered. Neither <code>null</code> nor empty.
   * @param writer       The writer which is used to receive the generated content. Not <code>null</code>.
   * @param params       Parameters that will be used while processing the template. Not <code>null</code>.
   * 
   * @throws FreemarkerException   In case of any error.
   */
  public void generate( @NonNull FreemarkerContext descriptor, @NonNull String template, @NonNull Writer writer, @NonNull Map<String,Object> params ) {
    generate( descriptor, template, writer, params, null );
  }

  /**
   * Invokes the generation process of a template.
   * 
   * @param descriptor   The descriptor used to access the templates and some add ons. Not <code>null</code>.
   * @param name         The name of the template that is supposed to be rendered. Neither <code>null</code> nor empty.
   * @param writer       The writer which is used to receive the generated content. Not <code>null</code>.
   * @param model        Parameters that will be used while processing the template. Not <code>null</code>.
   * 
   * @throws FreemarkerException   In case of any error.
   */
  public void generate( @NonNull FreemarkerContext descriptor, @NonNull String template, @NonNull Writer writer, @NonNull TemplateModel model ) {
    generate( descriptor, template, writer, model, null );
  }

  /**
   * Invokes the generation process of a template.
   * 
   * @param descriptor   The descriptor used to access the templates and some add ons. Not <code>null</code>.
   * @param name         The name of the template that is supposed to be rendered. Neither <code>null</code> nor empty.
   * @param writer       The writer which is used to receive the generated content. Not <code>null</code>.
   * @param params       Parameters that will be used while processing the template. Maybe <code>null</code>.
   * @param locale       The locale which will be used upon selection of the template. Maybe <code>null</code>.
   * 
   * @throws FreemarkerException   In case of any error.
   */
  public void generate( @NonNull FreemarkerContext descriptor, @NonNull String name, @NonNull Writer writer, Map<String,Object> params, Locale locale ) {
    if( params == null ) {
      params = Collections.emptyMap();
    }
    generateImpl( descriptor, name, writer, params, locale );
  }

  /**
   * Invokes the generation process of a template.
   * 
   * @param descriptor   The descriptor used to access the templates and some add ons. Not <code>null</code>.
   * @param name         The name of the template that is supposed to be rendered. Neither <code>null</code> nor empty.
   * @param writer       The writer which is used to receive the generated content. Not <code>null</code>.
   * @param model        Parameters that will be used while processing the template. Maybe <code>null</code>.
   * @param locale       The locale which will be used upon selection of the template. Maybe <code>null</code>.
   * 
   * @throws FreemarkerException   In case of any error.
   */
  public void generate( @NonNull FreemarkerContext descriptor, @NonNull String name, @NonNull Writer writer, TemplateModel model, Locale locale ) {
    if( model == null ) {
      model = Constants.EMPTY_HASH;
    }
    generateImpl( descriptor, name, writer, model, locale );
  }

  private void generateImpl( FreemarkerContext descriptor, String name, Writer writer, Object model, Locale locale ) {
    try {
      Template template = getTemplate( descriptor, name, locale != null ? locale : Locale.ENGLISH );
      template.process( model, writer );
      writer.flush();
    } catch( Exception ex ) {
      throw newException( ex, failed_to_process_template.format( name, ex.getLocalizedMessage() ) );
    }
  }

  /**
   * Creates a new Exception based upon the supplied message. 
   * 
   * @param cause     The cause of the error. Maybe <code>null</code>.
   * @param message   A message indicating the cause of error. Neither <code>null</code> nor empty.
   * 
   * @return   A newly created exception indicating the error. Not <code>null</code>.
   */
  private FreemarkerException newException( Exception cause, String message ) {
    if( cause instanceof FreemarkerException ) {
      return (FreemarkerException) cause;
    }
    if( cause != null ) {
      log.error( message, cause );
      return new FreemarkerException( message, cause );
    } else {
      log.error( message );
      return new FreemarkerException( message );
    }
  }

  /**
   * Creates a new Exception based upon the supplied message. 
   *
   * @param missing   <code>true</code> <=> The cause is a missing template.
   * @param message   A message indicating the cause of error. Neither <code>null</code> nor empty.
   * 
   * @return   A newly created exception indicating the error. Not <code>null</code>.
   */
  private FreemarkerException newException( boolean missing, String message ) {
    log.error( message );
    return new FreemarkerException( message, missing );
  }

} /* ENDCLASS */
