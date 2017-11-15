package edu.ncsu.csc.itrust2.apitest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import edu.ncsu.csc.itrust2.config.RootConfiguration;
import edu.ncsu.csc.itrust2.forms.ChangePasswordForm;
import edu.ncsu.csc.itrust2.forms.EmailForm;
import edu.ncsu.csc.itrust2.forms.personnel.PersonnelForm;
import edu.ncsu.csc.itrust2.models.enums.Role;
import edu.ncsu.csc.itrust2.models.persistent.PasswordResetToken;
import edu.ncsu.csc.itrust2.models.persistent.Personnel;
import edu.ncsu.csc.itrust2.models.persistent.User;
import edu.ncsu.csc.itrust2.mvc.config.WebMvcConfiguration;

/**
 * Tests for UC11 password functionality
 *
 * @author WillGlas
 *
 */
@RunWith ( SpringJUnit4ClassRunner.class )
@ContextConfiguration ( classes = { RootConfiguration.class, WebMvcConfiguration.class } )
@WebAppConfiguration
public class ForgotPasswordControllerTest {

    private MockMvc               mvc;

    @Autowired
    private WebApplicationContext context;

    /**
     * Sets up test
     */
    @Before
    public void setup () {
        mvc = MockMvcBuilders.webAppContextSetup( context ).build();
        final MockHttpServletRequest request = new MockHttpServletRequest( null, "localhost:8080/iTrust2" );
        RequestContextHolder.setRequestAttributes( new ServletRequestAttributes( request ) );
    }

    /**
     * Test password recovery endpoint
     *
     * @throws Exception
     */
    @Test
    public void testPasswordRecovery () throws Exception {
        final User u = new User( "pwtest", "$2a$10$EblZqNptyYvcLm/VwDCVAuBjzZOI7khzdyGPBr08PpIi0na624b8.",
                Role.ROLE_HCP, 1 );

        final PersonnelForm pForm = new PersonnelForm();
        pForm.setFirstName( "bad" );
        pForm.setLastName( "hcp" );
        pForm.setCity( "Raleigh" );
        pForm.setState( "NC" );
        pForm.setZip( "27606" );
        pForm.setAddress1( "Doesn't exist" );
        pForm.setPhone( "555-555-5555" );
        pForm.setEmail( "badmemoryhcp@gmail.com" );
        pForm.setSelf( "pwtest" );
        pForm.setEnabled( "1" );
        final Personnel p = new Personnel( pForm );
        p.save();

        // **TEST** Test the password recovery endpoint
        final EmailForm rForm = new EmailForm();
        rForm.setEmail( "badmemoryhcp@gmail.com" );
        mvc.perform( post( "/password/passwordrecovery" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( rForm ) ) ).andExpect( status().isOk() );

        // **TEST** Test the validate endpoint
        final Calendar c = Calendar.getInstance();
        c.add( Calendar.MINUTE, 30 );
        // Create then save a token
        final PasswordResetToken token = new PasswordResetToken( u, "token", c );
        token.save();

        mvc.perform( get( "/password/validate" ).param( "id", "pwtest" ).param( "token", "token" ) )
                .andExpect( redirectedUrl( "/resetPassword" ) ).andReturn();

        mvc.perform( get( "/resetPassword" ) ).andExpect( status().isOk() );

        final ChangePasswordForm cpForm = new ChangePasswordForm();
        cpForm.setOldPassword( "123456" );
        cpForm.setNewPassword( "1234567" );
        mvc.perform( post( "/password/change" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( cpForm ) ) ).andExpect( status().isOk() );
    }
}
