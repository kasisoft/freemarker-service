package com.kasisoft.cdi.services.freemarker;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import com.kasisoft.cdi.testbasis.*;

import freemarker.cache.*;

import org.testng.annotations.*;

import javax.annotation.*;
import javax.ejb.*;
import javax.inject.*;

import java.util.*;

import java.io.*;

/**
 * @author daniel.kasmeroglu@kasisoft.net
 */
@ManagedBean
public class FreemarkerServiceTest extends AbstractEjbTest {

  @Inject
  private FreemarkerService       freemarkerService;

  private GenerationDescriptor    descriptor;

  @BeforeSuite
  public void prepare() {
    descriptor = new GenerationDescriptor();
    descriptor.getTemplateLoader().add( new ClassTemplateLoader( FreemarkerServiceTest.class, "/templates" ) );
  }
  
  @DataProvider(name = "invalidGenerateCallData")
  public Object[][] invalidGenerateCallData() {
    return new Object[][] {
      { null, "sample", new StringWriter() },
      { descriptor, null, new StringWriter() },
      { descriptor, "sample", null },
    };
  }
  
  @Test(expectedExceptions = EJBException.class, dataProvider = "invalidGenerateCallData")
  public void invalidGenerateCall1( GenerationDescriptor descriptor, String template, Writer writer ) {
    freemarkerService.generate( descriptor, template, writer );
  }

  @Test(expectedExceptions = EJBException.class, dataProvider = "invalidGenerateCallData")
  public void invalidGenerateCall2( GenerationDescriptor descriptor, String template, Writer writer ) {
    freemarkerService.generate( descriptor, template, writer, null );
  }

  @Test(expectedExceptions = EJBException.class, dataProvider = "invalidGenerateCallData")
  public void invalidGenerateCall3( GenerationDescriptor descriptor, String template, Writer writer ) {
    freemarkerService.generate( descriptor, template, writer, null, null );
  }

  @DataProvider(name = "generateSimpleData")
  public Object[][] generateSimpleData() {
    return new Object[][] {
      { "simple/01_singleline.ftl"          , "Hello World"         },
      { "simple/02_multiline.ftl"           , "This is\nawesome"    },
      { "simple/03_multiline_with_gap.ftl"  , "This is\n\nawesome"  },
    };
  }

  @Test(dataProvider = "generateSimpleData")
  public void generateSimple( String template, String expected ) {
    StringWriter writer = new StringWriter();
    freemarkerService.generate( descriptor, template, writer );
    assertThat( writer.toString(), is( expected ) );
  }

  @DataProvider(name = "generateVarsData")
  public Object[][] generateVarsData() {
    return new Object[][] {
      { "vars/01_singleline.ftl", newMap( "name", "Peter"  ), "Hello Peter"   },
      { "vars/01_singleline.ftl", newMap( "name", "Gustav" ), "Hello Gustav"  },
    };
  }
  
  @Test(dataProvider = "generateVarsData")
  public void generateVars( String template, Map<String,Object> params, String expected ) {
    StringWriter writer = new StringWriter();
    freemarkerService.generate( descriptor, template, writer, params );
    assertThat( writer.toString(), is( expected ) );
  }

  @DataProvider(name = "generateWithMacrosData")
  public Object[][] generateWithMacrosData() {
    return new Object[][] {
      { "inclusions/01_example.ftl", newMap( "name", "Peter"  ), "VALUE: Peter-CONTENT\n"  },
      { "inclusions/01_example.ftl", newMap( "name", "Gustav" ), "VALUE: Gustav-CONTENT\n" },
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