package edu.ncsu.csc.itrust2.apitest;

import static org.junit.Assert.assertEquals;
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
import edu.ncsu.csc.itrust2.models.persistent.OfficeVisit;
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
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
    }

    /**
     * Test the endpoint for creating NDCCode
     *
     * @throws Exception
     */
    @Test
    public void testCreateNDCCode () throws Exception {

        final CodeForm c1 = createCodeForm( "1234-5678-90", "Viagra" );
        final MvcResult result = mvc.perform( post( "/api/v1/ndccodes" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( c1 ) ) ).andExpect( status().isOk() ).andReturn();
        final JsonObject jsonObject = gson.fromJson( result.getResponse().getContentAsString(), JsonObject.class );
        assertEquals( "1234-5678-90", jsonObject.get( "code" ).getAsString() );
        assertEquals( "Viagra", jsonObject.get( "name" ).getAsString() );
    }

    /**
     * Test the endpoint for creating prescriptions
     *
     * @throws Exception
     */
    @Test
    public void testCreatePrescription () throws Exception {

        // Get an office visit that we can add a prescription to
        final Long ovId = OfficeVisit.getForPatient( "antti" ).get( 0 ).getId();

        final PrescriptionForm p1 = createPrescriptionForm( "34.1", "10/22/2017", "10/22/2018", "1234-5678-90", "antti",
                "10" );
        p1.setOfficeVisitId( ovId + "" );

        MvcResult result = mvc.perform( post( "/api/v1/prescriptions" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( p1 ) ) ).andExpect( status().isOk() ).andReturn();
        JsonObject jsonObject = gson.fromJson( result.getResponse().getContentAsString(), JsonObject.class );
        verifyPrescriptionJson( jsonObject, "antti", "34.1", "1508644800000", "1540180800000", "10", "1234-5678-90",
                "Viagra" );
        final String pId = jsonObject.get( "id" ).getAsString();

        final PrescriptionForm p2 = createPrescriptionForm( "2.99", "9/22/2015", "3/20/2018", "1111-1111-11", "antti",
                "16" );

        result = mvc.perform( post( "/api/v1/prescriptions" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( p2 ) ) ).andExpect( status().isOk() ).andReturn();
        jsonObject = gson.fromJson( result.getResponse().getContentAsString(), JsonObject.class );
        verifyPrescriptionJson( jsonObject, "antti", "2.99", "1442894400000", "1521518400000", "16", "1111-1111-11",
                "Oxicodon" );

        result = mvc.perform( get( "/api/v1/prescriptions/antti" ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andReturn();

        final JsonArray jsonArray = gson.fromJson( result.getResponse().getContentAsString(), JsonArray.class );
        final JsonObject jsonObj1 = jsonArray.get( jsonArray.size() - 2 ).getAsJsonObject();
        verifyPrescriptionJson( jsonObj1, "antti", "34.1", "1508644800000", "1540180800000", "10", "1234-5678-90",
                "Viagra" );

        final JsonObject jsonObj2 = jsonArray.get( jsonArray.size() - 1 ).getAsJsonObject();
        verifyPrescriptionJson( jsonObj2, "antti", "2.99", "1442894400000", "1521518400000", "16", "1111-1111-11",
                "Oxicodon" );

        result = mvc.perform( put( "/api/v1/prescriptions/renew/" + pId ).contentType( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andReturn();
        jsonObject = gson.fromJson( result.getResponse().getContentAsString(), JsonObject.class );
        verifyPrescriptionJson( jsonObject, "antti", "34.1", "1508644800000", "1540180800000", "9", "1234-5678-90",
                "Viagra" );
    }

    private void verifyPrescriptionJson ( final JsonObject jsonObject, final String patient, final String dosage,
            final String start, final String end, final String renewals, final String code, final String name ) {
        assertEquals( patient, jsonObject.get( "patient" ).getAsString() );
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