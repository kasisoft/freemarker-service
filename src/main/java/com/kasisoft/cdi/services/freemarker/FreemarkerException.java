package com.kasisoft.cdi.services.freemarker;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class FreemarkerException extends RuntimeException {

  public FreemarkerException() {
    super();
  }

  public FreemarkerException( String message, Throwable cause ) {
    super( message, cause );
  }

  public FreemarkerException( String message ) {
    super( message );
  }

  public FreemarkerException( Throwable cause ) {
    super( cause );
  }

  protected FreemarkerException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
    super( message, cause, enableSuppression, writableStackTrace );
  }

} /* ENDCLASS */
