package org.sola.opentenure.services.boundary.beans.claim;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.sola.common.ClaimStatusConstants;
import org.sola.common.RolesConstants;
import org.sola.common.StringUtility;
import org.sola.cs.services.ejbs.claim.businesslogic.ClaimEJBLocal;
import org.sola.cs.services.ejbs.claim.entities.Claim;
import org.sola.cs.services.ejbs.claim.entities.Restriction;
import org.sola.opentenure.services.boundary.beans.AbstractBackingBean;
import org.sola.opentenure.services.boundary.beans.exceptions.OTWebException;
import org.sola.opentenure.services.boundary.beans.helpers.ErrorKeys;
import org.sola.opentenure.services.boundary.beans.helpers.MessageBean;
import org.sola.opentenure.services.boundary.beans.helpers.MessageProvider;
import org.sola.opentenure.services.boundary.beans.helpers.MessagesKeys;
import org.sola.opentenure.services.boundary.beans.language.LanguageBean;
import org.sola.services.common.logging.LogUtility;

/**
 * Provides methods and properties for making claims merge or split
 */
@Named
@ViewScoped
public class ClaimsMutationBean extends AbstractBackingBean {

    @EJB
    ClaimEJBLocal claimEjb;

    @Inject
    MessageProvider msgProvider;

    @Inject
    LanguageBean langBean;

    @Inject
    MessageBean msg;

    private List<Claim> oldClaims;
    private List<Claim> newClaims;
    private boolean completed;
    private boolean isMerge = true;
    private String claimIdToAdd;

    public ClaimsMutationBean() {
        super();
    }

    @PostConstruct
    private void init() {
        oldClaims = new ArrayList<>();
        newClaims = new ArrayList<>();
        isMerge = StringUtility.empty(getRequestParam("type")).equalsIgnoreCase("merge");
        completed = false;
    }

    public Claim[] getOldClaims() {
        if (oldClaims != null) {
            return oldClaims.toArray(new Claim[oldClaims.size()]);
        }
        return new Claim[0];
    }

    public boolean getCompleted() {
        return completed;
    }

    public List<Claim> getNewClaims() {
        return newClaims;
    }

    public boolean getIsMerge() {
        return isMerge;
    }

    public String getClaimIdToAdd() {
        return claimIdToAdd;
    }

    public void setClaimIdToAdd(String claimIdToAdd) {
        this.claimIdToAdd = claimIdToAdd;
    }

    public void addOldClaim() {
        if (!StringUtility.isEmpty(claimIdToAdd) && !checkClaimAlreadyAdded()) {
            Claim claim = claimEjb.getClaim(claimIdToAdd);
            if (checkClaimToAdd(claim)) {
                oldClaims.add(claim);
            }
        }
    }

    public void deleteOldClaim(String claimId) {
        for (int i = 0; i < oldClaims.size(); i++) {
            if (oldClaims.get(i).getId().equalsIgnoreCase(claimId)) {
                oldClaims.remove(i);
                break;
            }
        }
    }

    public void addNewClaim() {
        if (!StringUtility.isEmpty(claimIdToAdd) && !checkClaimAlreadyAdded()) {
            Claim claim = claimEjb.getClaim(claimIdToAdd);
            if (checkClaimToAdd(claim)) {
                newClaims.add(claim);
            }
        }
    }

    public void deleteNewClaim(String claimId) {
        for (int i = 0; i < newClaims.size(); i++) {
            if (newClaims.get(i).getId().equalsIgnoreCase(claimId)) {
                newClaims.remove(i);
                break;
            }
        }
    }

    public boolean getShowAddOldClaim() {
        if (!completed) {
            if ((!isMerge && oldClaims.size() < 1) || (isMerge)) {
                return true;
            }
        }
        return false;
    }

    public boolean getShowAddNewClaim() {
        if (!completed) {
            if ((isMerge && newClaims.size() < 1) || (!isMerge)) {
                return true;
            }
        }
        return false;
    }

    private boolean checkClaimToAdd(Claim claim) {
        if (claim == null) {
            throw new OTWebException(msgProvider.getErrorMessage(ErrorKeys.CLAIM_NOT_FOUND));
        }
        // Check status 
        if (!claim.getStatusCode().equalsIgnoreCase(ClaimStatusConstants.MODERATED)) {
            throw new OTWebException(msgProvider.getErrorMessage(ErrorKeys.CLAIM_MUST_BE_MODERATED));
        }
        // Check restrictions
        if (claim.getRestrictions() != null) {
            for (Restriction restriction : claim.getRestrictions()) {
                if (restriction.getStatus().equalsIgnoreCase("a")) {
                    throw new OTWebException(msgProvider.getErrorMessage(ErrorKeys.CLAIM_HAS_RESTRICTIONS));
                }
            }
        }
        return true;
    }

    private boolean checkClaimAlreadyAdded() {
        if (!StringUtility.isEmpty(claimIdToAdd)) {
            // Make sure it's not in any list
            for (Claim claim : newClaims) {
                if (claim.getId().equalsIgnoreCase(claimIdToAdd)) {
                    throw new OTWebException(msgProvider.getErrorMessage(ErrorKeys.CLAIM_ALREADY_IN_LIST));
                }
            }
            for (Claim claim : oldClaims) {
                if (claim.getId().equalsIgnoreCase(claimIdToAdd)) {
                    throw new OTWebException(msgProvider.getErrorMessage(ErrorKeys.CLAIM_ALREADY_IN_LIST));
                }
            }
        }
        return false;
    }

    public void checkCanAccess() throws Exception {
        if (!claimEjb.isInRole(RolesConstants.CS_MODERATE_CLAIM)) {
            throw new OTWebException(msgProvider.getErrorMessage(ErrorKeys.GENERAL_INSUFFICIENT_RIGHTS));
        }
    }

    public void merge() {
        if (completed) {
            throw new OTWebException(msgProvider.getErrorMessage(ErrorKeys.TRANSACTION_HAS_BEEN_COMPLETED));
        }

        if (newClaims.size() != 1 || oldClaims.size() < 2) {
            msg.setErrorMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIMS_MERGE_COUNT));
            return;
        }

        try {
            claimEjb.mergeClaims(oldClaims, newClaims.get(0));
            completed = true;
            msg.setSuccessMessage(msgProvider.getMessage(MessagesKeys.CLAIMS_MERGE_SUCCESS));
        } catch (Exception e) {
            LogUtility.log("Failed to merge claims", e);
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }

    public void split() {
        if (completed) {
            throw new OTWebException(msgProvider.getErrorMessage(ErrorKeys.TRANSACTION_HAS_BEEN_COMPLETED));
        }

        if (newClaims.size() < 2 || oldClaims.size() != 1) {
            msg.setErrorMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_SPLIT_COUNT));
            return;
        }

        try {
            claimEjb.splitClaim(oldClaims.get(0), newClaims);
            completed = true;
            msg.setSuccessMessage(msgProvider.getMessage(MessagesKeys.CLAIM_SPLIT_SUCCESS));
        } catch (Exception e) {
            LogUtility.log("Failed to split claim", e);
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }
}
