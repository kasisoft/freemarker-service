package com.kasisoft.cdi.services.freemarker;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.kasisoft.cdi.weldex.*;

import org.testng.annotations.*;

import java.util.*;

import java.io.*;

import freemarker.cache.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
public class SquareTest {

  FreemarkerService    freemarkerService;
  FreemarkerContext    descriptor;

  @BeforeSuite
  public void prepare() {
    freemarkerService = CdiContext.component( FreemarkerService.class );
    descriptor        = new FreemarkerContext();
    descriptor.setAngularBrackets( false );
    descriptor.getTemplateLoader().add( new ClassTemplateLoader( FreemarkerServiceTest.class, "/templates" ) );
  }

  @DataProvider(name = "generateWithMacrosData")
  public Object[][] generateWithMacrosData() {
    return new Object[][] {
      { "square/01_example.ftl", newMap( "name", "Peter"  ), "VALUE: Peter-CONTENT\n"  },
      { "square/01_example.ftl", newMap( "name", "Gustav" ), "VALUE: Gustav-CONTENT\n" },
    };
  }

  @Test(dataProvider = "generateWithMacrosData")
  public void generateWithMacros( String template, Map<String,Object> params, String expected ) {
    StringWriter writer = new StringWriter();
    freemarkerService.generate( descriptor, template, writer, params );
    assertThat( writer.toString(), is( expected ) );
  }

  private Map<String,Object> newMap( Object ... args ) {
    Map<String,Object> result = new HashMap<>();
    for( int i = 0; i < args.length; i += 2 ) {
      result.put( (String) args[i], args[ i + 1 ] );
    }
    return result;
  }

} /* ENDCLASS */
