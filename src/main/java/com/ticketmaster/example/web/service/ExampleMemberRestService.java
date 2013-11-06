package com.ticketmaster.example.web.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ticketmaster.example.web.model.Member;


@Path("/")
public interface ExampleMemberRestService {

    static final String ID = "id";
    
    
	
    /**
     * Looks up a member by Id
     *
     * @param id
     *            the unique identifier of the member to be looked up
     * @return Member
     * @throws Exception
     *             an error that occured during retrieving the member
     */
    @GET
    @Path("/{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })	
    Member getMember(@PathParam(ID) final long memberId) throws Exception;

    /**
     * Creates a permanent record of the given Member object
     *
     * @param inMember
     *            the Member object to be stored/created
     * @return the Member object that was successfully stored/created
     * @throws Exception
     *             an error that occurred during the creation of the member
     */
    @POST
	@Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })	
    Member createMember(final Member inMember) throws Exception;

    /**
     * Deletes the record associated with the given Member unique identifier
     *
     * @param memberId
     *            the id for the Member to be deleted
     * @return a status code of "SUCCESS" when the operation completes successfully
     * @throws Exception
     *             an error that occurred during the deletion of the member
     */
    @DELETE
    @Path("/{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })	
    void deleteMember(@PathParam(ID) final long memberId) throws Exception;


    /**
     * Updates the given Member
     *
     * @param memberId
     *          the id for the Member to be updaetd
     * @param inMember
     *          the Member object to be updated
     * @return
     * @throws Exception
     *             an error that occured during the updating of the member
     */
    @PUT
    @Path("/{id}")
	@Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })	
    Member updateMember(@PathParam(ID) final long memberId, final Member inMember) throws Exception;


    /**
     * Looks up the total number of Member records in the system.
     *
     * @return Member count
     */
    @GET
    @Path("/count")
	@Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })	
    long getMemberCount() throws Exception;


}
