repositories.remote << "http://www.ibiblio.org/maven2/"
repositories.remote << 'file:///Users/jimmy/projects/private/limma/lib'


LIBS = 'asm:asm-attrs:jar:1.5.3', 'asm:asm:jar:1.5.3', 'com.thoughtworks.xstream:xstream:jar:1.2.2', 'commons-codec:commons-codec:jar:1.3', 'commons-collections:commons-collections:jar:3.0', 'commons-httpclient:commons-httpclient:jar:3.0-rc3', 'commons-io:commons-io:jar:1.0', 'commons-lang:commons-lang:jar:2.0', 'commons-logging:commons-logging:jar:1.0.3', 'dom4j:dom4j:jar:1.6', 'java_mp3:java_mp3:jar:0.3', 'jflac:jflac:jar:1.2', 'jl:jl:jar:1.0', 'jmock:jmock:jar:1.0.1', 'jogg:jogg:jar:0.0.7', 'jogl:gluegen-rt:jar:1.1.0', 'jogl:jogl:jar:1.1.0', 'jorbis:jorbis:jar:0.0.15', 'junit:junit:jar:3.8.1', 'miglayout:miglayout:jar:3.0', 'mp3spi:mp3spi:jar:1.9.4', 'nekohtml:nekohtml:jar:0.9.5', 'org.picocontainer:picocontainer:jar:1.3', 'rhino:js:jar:1.6R1', 'saxpath:saxpath:jar:1.0-FCS', 'timingframework:timingframework:jar:1.0-SNAPSHOT', 'tritonus:tritonus_jorbis:jar:0.3.6', 'tritonus:tritonus_remaining:jar:0.3.6', 'tritonus:tritonus_share:jar:0.3.6', 'xerces:xerces:jar:2.4.0', 'xerces:xercesImpl:jar:2.6.2', 'xerces:xmlParserAPIs:jar:2.2.1', 'xml-apis:xml-apis:jar:1.0.b2', 'xpp3:xpp3_min:jar:1.1.3.4.O', 'jaxen:jaxen:jar:1.1-beta-6', 'htmlunit:htmlunit:jar:1.7'

desc 'Limma HTPC frontend'



define 'limma' do
    project.group = 'jimmy'
    project.version = '0.0.1'
    compile.using :target=>"1.5", :other=> ['-encoding', 'utf8']
    compile.with LIBS
    test.using :reports=>'target/reports'
    test.with 'cglib:cglib-nodep:jar:2.1_2', 'com.agical.rmock:rmock:jar:2.0.0-rc-6'

    package :jar, :id => 'limma'


    package(:tgz).path("#{id}-#{version}").tap do |path|
      path.include _("resources")
      path.include _("config")
      path.include "limma.sh"
    end

    package(:tgz).path("#{id}-#{version}/lib").tap do |path|
      path.include artifacts(LIBS)
      path.include "target/#{id}-#{version}.jar"
    end
end