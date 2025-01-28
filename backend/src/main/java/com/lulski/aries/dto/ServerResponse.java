package com.lulski.aries.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lulski.aries.user.User;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * DTO for UserController
 */
@SuppressFBWarnings(value = "EI_EXPOSE_REP2")
public class ServerResponse {
    private static final Logger logger = LoggerFactory.getLogger(ServerResponse.class);
    private User item;
    private ServerErrorResponse error;

    public ServerResponse() {
    }

    public ServerResponse(User item) {
        this.item = item;
    }

    public ServerResponse(ServerErrorResponse errorResponse) {
        this.error = errorResponse;
    }

    /**
     * SuppressFBWarnings because there is a serialization process that went haywire
     * if errorCopy is used
     *
     * @return
     */
    @SuppressFBWarnings
    public ServerErrorResponse getError() {
        // var errorCopy = new ServerErrorResponse(null, null, 0);
        // if (this.error != null) {
        // errorCopy.setDetails(this.error.getDetails());
        // errorCopy.setErrorCode(this.error.getErrorCode());
        // errorCopy.setMessage(this.error.getMessage());
        // logger.info("errorCopy: " + errorCopy.getErrorCode());
        // }
        // return errorCopy;
        logger.info("getError is called ");
        return error;
    }

    public void setError(ServerErrorResponse error) {
        this.error = error;
    }

    public User getItem() {
        var itemCopy = new User(null, null, null, null, null, null);
        if (this.item != null) {
            itemCopy.setArchived(this.item.isArchived());
            itemCopy.setEmail(this.item.getEmail());
            itemCopy.setFirstName(this.item.getFirstName());
            itemCopy.setLastName(this.item.getLastName());
            itemCopy.setUsername(this.item.getUsername());
            itemCopy.setId(this.item.getId());
            logger.info(">>>>>>> itemCopy: " + itemCopy.getId().toString());
        }
        return itemCopy;
    }

    public void setItem(User item) {
        this.item = item;
    }
}
