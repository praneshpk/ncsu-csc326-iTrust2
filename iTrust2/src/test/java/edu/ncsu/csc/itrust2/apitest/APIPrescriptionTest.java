package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.ParseException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.admin.CodeForm;
import edu.ncsu.csc.itrust2.forms.hcp.PrescriptionForm;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Test class for APIPrescriptionController
 *
 * @author WillGlas
 */

@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class APIPrescriptionTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    private final Gson            gson = new Gson();

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context )// .apply(
                                                           // SecurityMockMvcConfigurers.springSecurity()
                                                           // )
                .build();
    }

    /**
     * Test the endpoint for creating NDCCode
     *
     * @throws Exception
     */
    @Test
    @WithMockUser ( username = "admin", authorities = { "ADMIN" } )
    public void testCreateNDCCode () throws Exception {
        final CodeForm c1 = createCodeForm( "1234-5678-90", "Viagra" );
        final MvcResult result = mvc
                .perform( post( "/api/v1/ndccodes" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( c1 ) ).with( csrf() ) )
                .andExpect( status().isOk() ).andReturn();
        final JsonObject jsonObject = gson.fromJson( result.getResponse().getContentAsString(), JsonObject.class );
        assertEquals( "1234-5678-90", jsonObject.get( "code" ).getAsString() );
        assertEquals( "Viagra", jsonObject.get( "name" ).getAsString() );

        final CodeForm c2 = createCodeForm( "1111-1111-11", "Oxicodon" );
        mvc.perform( post( "/api/v1/ndccodes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c2 ) ).with( csrf() ) ).andExpect( status().isOk() ).andReturn();
    }

    /**
     * Test the endpoint for creating prescriptions
     *
     * @throws Exception
     */
    @Test
    @WithMockUser // ( username = "hcp", roles = { "HCP", "PATIENT" } )
    public void testCreatePrescription () throws Exception {

        final PrescriptionForm p1 = createPrescriptionForm( "34.1", "10/22/2017", "10/22/2018", "1234-5678-90",
                "AliceThirteen", "10" );

        MvcResult result = mvc
                .perform( post( "/api/v1/prescriptions" ).contentType( MediaType.APPLICATION_JSON )
                        .content( TestUtils.asJsonString( p1 ) ).with( csrf() ) )
                .andExpect( status().isOk() ).andReturn();
        JsonObject jsonObject = gson.fromJson( result.getResponse().getContentAsString(), JsonObject.class );
        verifyPrescriptionJson( jsonObject, "AliceThirteen", "34.1", "1508644800000", "1540180800000", "10",
                "1234-5678-90", "Viagra" );
        final String pId = jsonObject.get( "id" ).getAsString();

        final PrescriptionForm p2 = createPrescriptionForm( "2.99", "9/22/2015", "3/20/2018", "1111-1111-11",
                "AliceThirteen", "16" );

        result = mvc.perform( post( "/api/v1/prescriptions" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( p2 ) ) ).andExpect( status().isOk() ).andReturn();
        jsonObject = gson.fromJson( result.getResponse().getContentAsString(), JsonObject.class );
        verifyPrescriptionJson( jsonObject, "AliceThirteen", "2.99", "1442894400000", "1521518400000", "16",
                "1111-1111-11", "Oxicodon" );

        result = mvc.perform( get( "/api/v1/prescriptions/patient" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andReturn();

        final JsonArray jsonArray = gson.fromJson( result.getResponse().getContentAsString(), JsonArray.class );
        final JsonObject jsonObj1 = jsonArray.get( jsonArray.size() - 2 ).getAsJsonObject();
        verifyPrescriptionJson( jsonObj1, "AliceThirteen", "34.1", "1508644800000", "1540180800000", "10",
                "1234-5678-90", "Viagra" );

        final JsonObject jsonObj2 = jsonArray.get( jsonArray.size() - 1 ).getAsJsonObject();
        verifyPrescriptionJson( jsonObj2, "AliceThirteen", "2.99", "1442894400000", "1521518400000", "16",
                "1111-1111-11", "Oxicodon" );

        result = mvc.perform( put( "/api/v1/prescriptions/renew/" + pId ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andReturn();
        jsonObject = gson.fromJson( result.getResponse().getContentAsString(), JsonObject.class );
        verifyPrescriptionJson( jsonObject, "AliceThirteen", "34.1", "1508644800000", "1540180800000", "9",
                "1234-5678-90", "Viagra" );
    }

    private void verifyPrescriptionJson ( final JsonObject jsonObject, final String patient, final String dosage,
            final String start, final String end, final String renewals, final String code, final String name ) {
        assertEquals( patient, jsonObject.get( "patient" ).getAsJsonObject().get( "self" ).getAsString() );
        assertEquals( dosage, jsonObject.get( "dosage" ).getAsString() );
        assertEquals( start, jsonObject.get( "start" ).getAsString() );
        assertEquals( end, jsonObject.get( "end" ).getAsString() );
        assertEquals( renewals, jsonObject.get( "renewals" ).getAsString() );
        assertEquals( code, jsonObject.get( "ndcCode" ).getAsJsonObject().get( "code" ).getAsString() );
        assertEquals( name, jsonObject.get( "ndcCode" ).getAsJsonObject().get( "name" ).getAsString() );
    }

    /**
     * Create and return a CodeForm object
     *
     * @param id
     *            id of object
     * @param name
     *            name of object
     * @return CodeForm
     */
    private CodeForm createCodeForm ( final String id, final String name ) {
        final CodeForm cf = new CodeForm();
        cf.setId( id );
        cf.setName( name );
        return cf;
    }

    /**
     * Create and return a PrescriptionForm
     *
     * @param dosage
     *            dosage
     * @param start
     *            start date
     * @param end
     *            end date
     * @param ndcCode
     *            Id of NDCCode object
     * @param patient
     *            Username of patient
     * @param renewals
     *            number of renewals
     * @return Prescription object with given properties
     * @throws ParseException
     */
    private PrescriptionForm createPrescriptionForm ( final String dosage, final String start, final String end,
            final String ndcCode, final String patient, final String renewals ) throws ParseException {
        final PrescriptionForm pf = new PrescriptionForm();
        pf.setDosage( dosage );
        pf.setStart( start );
        pf.setEnd( end );
        pf.setNdcCode( ndcCode );
        pf.setPatient( patient );
        pf.setRenewals( renewals );
        return pf;
    }

}
