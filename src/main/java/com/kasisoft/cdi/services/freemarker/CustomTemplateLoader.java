package com.kasisoft.cdi.services.freemarker;

import freemarker.cache.*;

import java.util.*;

import java.io.*;

import lombok.*;
import lombok.experimental.*;

/**
 * Variety of Freemarkers {@link MultiTemplateLoader} which is capable to be extended during it's lifetime.
 * 
 * @author daniel.kasmeroglu@kasisoft.net
 */
@EqualsAndHashCode(of = "loaders") 
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class CustomTemplateLoader implements StatefulTemplateLoader {
  
  List<TemplateLoader>         loaders     = new LinkedList<>();
  Map<String, TemplateLoader>  lastLoaders = new HashMap<>();

  /**
   * Adds the supplied TemplateLoader instances to this one. Although it's possible to invoke this function after 
   * Freemarker generated some output it's advised to register the loaders ahead.
   * 
   * @param templateloaders   A list of loaders providing the templated. Not <code>null</code>.
   */
  public synchronized void add( @NonNull TemplateLoader ... templateloaders ) {
    resetState();
    for( TemplateLoader loader : templateloaders ) {
      loaders.add( loader );
    }
  }

  /**
   * Removes the supplied TemplateLoader instances from this one. Although it's possible to invoke this function after 
   * Freemarker generated some output it's advised to register the loaders ahead.
   * 
   * @param templateloaders   A list of loaders providing the templated. Not <code>null</code>.
   */
  public synchronized void remove( @NonNull TemplateLoader ... templateloaders ) {
    for( TemplateLoader loader : templateloaders ) {
      loaders.remove( loader );
    }
    resetState();
  }

  @Override
  public synchronized Object findTemplateSource( String name ) throws IOException {
    
    // try to request the last successful loader if there was one
    TemplateLoader lastLoader = lastLoaders.get( name );
    if( lastLoader != null ) {
      Object source = lastLoader.findTemplateSource( name );
      if( source != null ) {
        return new MultiSourceDescriptor( source, lastLoader );
      } else {
        // it's no longer available through this one
        lastLoaders.remove( name );
      }
    }

    // look for a loader that might provide the template
    for( TemplateLoader loader : loaders ) {
      if( loader == lastLoader ) {
        // we don't need to recheck the last loader (might be null)
        continue;
      }
      Object source = loader.findTemplateSource( name );
      if( source != null ) {
        lastLoaders.put( name, loader );
        return new MultiSourceDescriptor( source, loader );
      }
    }

    return null;
    
  }
  
  @Override
  public synchronized long getLastModified( Object descriptor ) {
    return ((MultiSourceDescriptor) descriptor).getLastModified();
  }

  @Override
  public synchronized Reader getReader( Object descriptor, String encoding ) throws IOException {
    return ((MultiSourceDescriptor) descriptor).getReader( encoding );
  }

  @Override
  public synchronized void closeTemplateSource( Object descriptor ) throws IOException {
    ((MultiSourceDescriptor) descriptor).close();
  }

  @Override
  public synchronized void resetState() {
    lastLoaders.clear();
    for( TemplateLoader loader : loaders ) {
      if( loader instanceof StatefulTemplateLoader ) {
        ((StatefulTemplateLoader) loader).resetState();
      }
    }
  }

  @Override
  public String toString() {
    return String.valueOf( loaders );
  }
  
  /**
   * Descriptor for a template source which is associated with it's loader.
   */
  @AllArgsConstructor
  @FieldDefaults(level = AccessLevel.PRIVATE)
  @EqualsAndHashCode
  private static final class MultiSourceDescriptor {

    Object          source;
    TemplateLoader  loader;

    public long getLastModified() {
      return loader.getLastModified( source );
    }

    public Reader getReader( String encoding ) throws IOException {
      return loader.getReader( source, encoding );
    }

    public void close() throws IOException {
      loader.closeTemplateSource( source );
    }

    @Override
    public String toString() {
      return String.valueOf( source );
    }
    
  } /* ENDCLASS */
  
} /* ENDCLASS */
