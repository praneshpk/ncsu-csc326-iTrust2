package edu.ncsu.csc.itrust2.apitest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.models.persistent.Diagnosis;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Tests API functionality for interacting with Diagnoses
 *
 * @author Cameron Estroff
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APIDiagnosisTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Test set up
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Tests DiagnosisAPI
     *
     * @throws Exception
     */
    @Test
    public void testDiagnosisAPI () throws Exception {
        final Diagnosis d = new Diagnosis( "name1", "H1.1" );
        mvc.perform( post( "/api/v1/creatediagnosis" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( d ) ) );
        mvc.perform( get( "/api/v1/diagnosis/name1" ) ).andExpect( status().isOk() )
                .andExpect( content().contentType( MediaType.APPLICATION_JSON_UTF8_VALUE ) );

        // Cannot create same diagnosis twice
        mvc.perform( post( "/api/v1/creatediagnosis" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( d ) ) ).andExpect( status().isConflict() );
    }
}
