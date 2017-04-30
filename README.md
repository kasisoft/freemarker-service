Purpose
=======

This library provides two services allowing to use Freemarker as a rendering engine for any kind
of content.

Contact
=======

* daniel.kasmeroglu@kasisoft.net


Requirements
============

 * Java 8


Jira
====

* https://kasisoft.com/jira/projects/CDIFM/issues


Maven
=====

I assume that you're familiar with Maven. If not I suggest to visit the following page:

* https://maven.apache.org/


Releases
--------

Releases can be found in Maven Central:

* http://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.kasisoft.cdi%22%20AND%20a%3A%22freemarker-service%22

     <dependency>
         <groupId>com.kasisoft.cdi</groupId>
        <artifactId>freemarker-service</artifactId>
        <version>0.8</version>
     </dependency>

Snapshots
---------

Snapshots can be used while accessing a dedicated maven repository. Your POM needs the following settings:

     <dependency>
       <groupId>com.kasisoft.cdi</groupId>
       <artifactId>freemarker-service</artifactId>
       <version>0.9-SNAPSHOT</version>
     </dependency>
     
     <repositories>
         <repository>
             <id>libs-kasisoft</id>
             <url>https://kasisoft.com/artifactory/libs-kasisoft</url>
             <releases>
                 <enabled>true</enabled>
             </releases>
             <snapshots>
                 <enabled>true</enabled>
             </snapshots>
         </repository>
     </repositories>
     
     
USAGE
=====

By instantiation:


    FreemarkerService           instantiated1 = new FreemarkerService();
    
    ExtendedFreemarkerService   instantiated2 = new ExtendedFreemarkerService();
    
By injection:

    @Inject
    FreemarkerService           injected1;
    
    @Inject
    ExtendedFreemarkerService   injected2;



License
=======

MIT License

Copyright (c) 2017 Daniel Kasmeroglu (Kasisoft)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
