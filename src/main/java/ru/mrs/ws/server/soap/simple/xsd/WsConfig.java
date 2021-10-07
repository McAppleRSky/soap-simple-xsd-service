package ru.mrs.ws.server.soap.simple.xsd;

import org.apache.cxf.tools.common.Processor;
import org.apache.cxf.tools.common.ToolConstants;
import org.apache.cxf.tools.common.ToolContext;
import org.apache.cxf.tools.common.ToolException;
import org.apache.cxf.tools.common.toolspec.parser.BadUsageException;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl4jDefinition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;
import org.apache.cxf.tools.misc.processor.XSDToWSDLProcessor;
import org.springframework.ws.wsdl.WsdlDefinition;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import static org.springframework.util.StringUtils.hasLength;
import static org.springframework.util.StringUtils.hasText;

@EnableWs
@Configuration
public class WsConfig extends WsConfigurerAdapter {

    @Bean
    public ServletRegistrationBean //dispatcherServlet
                                    createServletRegistrationBean
                                                                    (ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    @Bean(name = "countries")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema countriesSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("CountriesPort");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setTargetNamespace("http://spring.io/guides/gs-producing-web-service");
        wsdl11Definition.setSchema(countriesSchema);
        return wsdl11Definition;
    }

    @Bean
    public XsdSchema countriesSchema() {
        ClassPathResource xsdPathResource = new ClassPathResource("def/countries.xsd");
        return new SimpleXsdSchema(xsdPathResource);
    }

    @Bean
    public WsdlDefinition countriesDefinition() {
        String FILE_NAME = "countries"
                ,PATH = "def"
                ,XSD_EXT = ".xsd"
                ,WSDL_EXT = ".wsdl"
                ,resourcesMain = null
                ;
        ClassPathResource xsdPathResource = new ClassPathResource(PATH + FILE_NAME + XSD_EXT)
                ,wsdlPathResource = new ClassPathResource(PATH + FILE_NAME + WSDL_EXT)
                ;
        boolean existXsdRes = xsdPathResource.exists()
                ,existWsdlRes = wsdlPathResource.exists()
                ;
        if (existXsdRes && !existWsdlRes) {
            try {
                resourcesMain = xsdPathResource.getURL().getPath().split("/" + PATH + "/" + FILE_NAME + XSD_EXT)[0];
            } catch (IOException e) {
                e.printStackTrace();
            }
            XSDToWSDLProcessor processor = new XSDToWSDLProcessor();
            try {
                ToolContext env = new ToolContext();
                env.setParameters(new HashMap());
                env.put(ToolConstants.CFG_XSDURL, resourcesMain+"/"+PATH+"/"+FILE_NAME+XSD_EXT);
                env.put(ToolConstants.CFG_OUTPUTDIR, resourcesMain+"/"+PATH);
                processor.setEnvironment(env);
                processor.process();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            wsdlPathResource = new ClassPathResource(PATH + FILE_NAME + WSDL_EXT);
            existWsdlRes = wsdlPathResource.exists();
        }
        return null;
    }
}
