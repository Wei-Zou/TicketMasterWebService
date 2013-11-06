package com.ticketmaster.example.util;


import static com.ticketmaster.example.constants.ConstantDataManager.ERR_MEMBER;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ticketmaster.example.model.AddressEntity;
import com.ticketmaster.example.model.MemberEntity;

public class MemberValidator {

    private static final Logger LOG = LoggerFactory.getLogger(MemberValidator.class);


    public static final String ERR_UNABLE = "Unable to ";   
    /**
     * <code>validateMember</code>- Validates supplied member state.
     * 
     * @param member
     *            <code>Member</code> to validate
     * @param operation
     *            <code>String</code> operation to perform update/create
     * @throws BusinessException
     *             thrown when validation rule fails
     */
    public static void validateMember(final MemberEntity member, final String operation) 
    		throws Exception {

		// Member DateTimeCreated
		if (member.getDateTimeCreated() != null) {

		    errorHandler(operation, 
		    		"member: you cannot supply a DateTimeCreated value for a member", 
		    		"DateTimeCreated check failed for member - Could not",
		    		member.getDateTimeCreated());			    
		}
	
		final AddressEntity inAddress = member.getAddress();
	
		// Address DateTimeCreated
		if ((inAddress != null) && (inAddress.getDateTimeCreated() != null)) {

		    errorHandler(operation, 
		    		"member: you cannot supply a DateTimeCreated value for a member's address", 
		    		"DateTimeCreated check failed for member address - Could not",
		    		inAddress.getDateTimeCreated());	 
		}
	
		// Member DateTimeUpdated
		if (member.getDateTimeUpdated() != null) {
	
		    errorHandler(operation, 
		    		"member: you cannot supply a DateTimeUpdated value for a member", 
		    		"DateTimeUpdated check failed - Could not",
		    		member.getDateTimeUpdated());			    
		    
		}
	
		// Address DateTimeUpdated
		if ((inAddress != null) && (inAddress.getDateTimeUpdated() != null)) {

		    errorHandler(operation, 
		    		"member: you cannot supply a DateTimeUpdated value for a member's address", 
		    		"DateTimeUpdated check failed for member address - Could not",
		    		inAddress.getDateTimeUpdated());	
		    
		}
	
		// Member PersistenceVersion
		if (member.getPersistenceVersion() != null) {

		    errorHandler(operation, 
		    		"member: you cannot supply a PersistenceVersion value for a member when creating a new member", 
		    		"PersistenceVersion check failed - Could not",
		    		member.getPersistenceVersion());	 
		}
	
		// Address PersistenceVersion
		if ((inAddress != null) && (inAddress.getPersistenceVersion() != null)) {
	
		    errorHandler(operation, 
		    		"member: you cannot supply a PersistenceVersion value for a member's address when creating a new member", 
		    		"PersistenceVersion check failed for member address - Could not",
		    		inAddress.getPersistenceVersion());	    	  
			 		   
	    }
    }
    
    
    private static <T> void errorHandler(String operation, String logMsgDescription, String errorMsgDescription, T type)
    throws Exception
    {
    	logMessageHandler(operation, logMsgDescription, type);
    	
    	errorMessageHandler(operation, errorMsgDescription, type);
    }
    
    private static <T> void logMessageHandler(String operation, String description, T type)
    {
    	StringBuilder logMsg = new StringBuilder(ERR_UNABLE)    	
    	.append(operation)
    	.append(" ")
    	.append(description)
    	.append(", value=(")
    	.append(type.toString())
    	.append(")");
    	
    	LOG.debug(logMsg.toString());
    }
    
    private static <T> void errorMessageHandler(String operation, String errorMsgDescription, T type )
    throws Exception
    {
	    StringBuilder exceptionMsg = new StringBuilder(errorMsgDescription)
	    .append(" ")
	    .append(operation)
	    .append(" ")
	    .append(ERR_MEMBER)
	    .append(", value=(")
	    .append(type.toString())
	    .append(")");
	    
	    throw new Exception(exceptionMsg.toString());
    }
    
}
