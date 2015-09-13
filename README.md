GIT
---

*  ssh://git@kasisoft.com:7999/cdi/freemarker-service.git


COORDINATES
-----------

    <dependency>
        <groupId>com.kasisoft.cdi</groupId>
        <artifactId>freemarker-service</artifactId>
        <version>0.4-SNAPSHOT</version>
    </dependency>

USAGE
-----

Either bei injection or instantiation.

    FreemarkerService           instantiated1 = new FreemarkerService();
    
    ExtendedFreemarkerService   instantiated2 = new ExtendedFreemarkerService();
    ...
    instantiated2.postConstruct();
    
    @Inject
    FreemarkerService           injected1;
    
    @Inject
    ExtendedFreemarkerService   injected2;


TODO