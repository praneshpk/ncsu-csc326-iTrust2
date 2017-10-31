/*
 * Copyright 2002-2016 the original author or authors. Licensed under the Apache
 * License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package edu.ncsu.csc.itrust2.mvc.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.Ordered;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import edu.ncsu.csc.itrust2.models.persistent.Prescription;

/**
 * Maintains a variety of global configurations needed by the Spring web
 * application.
 *
 * @author Spring Security team
 * @author Kai Presler-Marshall
 *
 */
@EnableWebMvc
@ComponentScan ( { "edu.ncsu.csc.itrust2.controllers",
        "edu.ncsu.csc.itrust2.config" } ) /*
                                           * Controller packages. Update as
                                           * necessary
                                           */
@EnableGlobalMethodSecurity (
        prePostEnabled = true ) /*
                                 * Tell Spring to enforce the @PreAuthorize
                                 * annotations on Controller methods; this done
                                 * to ensure that only a user of the right type
                                 * has access to the page
                                 */
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

    /**
     * ApplicationContext used to maintain security
     */
    @Autowired
    private ApplicationContext          applicationContext;

    @Autowired
    private FormattingConversionService mvcConversionService;

    /**
     * Sets the /login path as the entry point for a user to login
     */
    @Override
    public void addViewControllers ( final ViewControllerRegistry registry ) {
        registry.addViewController( "/login" ).setViewName( "login" );
        registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
    }

    /**
     * Makes the Resources folder unprotected
     */
    @Override
    public void addResourceHandlers ( final ResourceHandlerRegistry registry ) {
        registry.addResourceHandler( "/resources/**" ).addResourceLocations( "classpath:/resources/" )
                .setCachePeriod( 31556926 );
        registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
    }

    /**
     * ViewResolver used for Thymeleaf's parsing
     *
     * @return ViewResolver generated
     */
    @Bean
    public ViewResolver viewResolver () {
        final ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine( templateEngine() );
        resolver.setCharacterEncoding( "UTF-8" );
        return resolver;
    }

    /**
     * Templating engine for Spring and Thymeleaf
     *
     * @return TemplateEngine generated
     */
    @Bean
    public TemplateEngine templateEngine () {
        final SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler( true );
        engine.setTemplateResolver( templateResolver() );
        return engine;
    }

    /**
     * Creates a TemplateResolver
     *
     * @return SpringResourceTemplateResolver generated
     */
    public SpringResourceTemplateResolver templateResolver () {

        final SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setPrefix( "classpath:/views/" );
        resolver.setSuffix( ".html" );
        resolver.setTemplateMode( TemplateMode.HTML );
        resolver.setApplicationContext( applicationContext );
        return resolver;
    }

    /**
     * Creates a DomainClassConverter from the mvcConversionService
     *
     * @return DomainClassConverter
     */
    @Bean
    public DomainClassConverter< ? > domainClassConverter () {
        return new DomainClassConverter<FormattingConversionService>( mvcConversionService );
    }

    /**
     * Configure the message converters
     *
     * @param converters
     *            converter list to add custom converters to
     */
    @Override
    public void configureMessageConverters ( final List<HttpMessageConverter< ? >> converters ) {
        converters.add( createGsonHttpMessageConverter() );
        super.configureMessageConverters( converters );
    }

    /**
     * Get a GsonHttpMessageConverter for converting a Prescription to JSON
     *
     * @return GsonHttpMessageConverter
     */
    private GsonHttpMessageConverter createGsonHttpMessageConverter () {
        final Gson gson = new GsonBuilder().registerTypeAdapter( Prescription.class, new PrescriptionTypeAdapter() )
                .create();

        final GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
        gsonConverter.setGson( gson );

        return gsonConverter;
    }

    /**
     * Adapter class so that prescriptions can be written out without creating
     * an infinite loop
     *
     * @author WillGlas
     */
    public class PrescriptionTypeAdapter extends TypeAdapter<Prescription> {
        /**
         * This method shouldn't ever need to be used
         *
         * @param reader
         *            JsonReader
         * @return null
         */
        @Override
        public Prescription read ( final JsonReader reader ) throws IOException {
            return null;
        }

        /**
         * Write out the object
         *
         * @param writer
         *            writer object
         * @param obj
         *            instance of Prescription
         */
        @Override
        public void write ( final JsonWriter writer, final Prescription obj ) throws IOException {
            if ( obj == null ) {
                writer.nullValue();
                return;
            }
            writer.beginObject();
            writer.name( "id" ).value( obj.getId() );
            writer.name( "patient" ).value( obj.getPatient().getId() );
            writer.name( "ndcCode" ).beginObject();
            writer.name( "code" ).value( obj.getNdcCode().getId() );
            writer.name( "name" ).value( obj.getNdcCode().getName() );
            writer.endObject();
            writer.name( "dosage" ).value( obj.getDosage() );
            writer.name( "start" ).value( obj.getStart().getTime() );
            writer.name( "end" ).value( obj.getEnd().getTime() );
            writer.name( "renewals" ).value( obj.getRenewals() );
            if ( obj.getOfficeVisit() != null ) {
                writer.name( "officeVisit" ).value( obj.getOfficeVisit().getId() );
            }
            writer.endObject();
        }
    }
}
