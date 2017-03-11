package org.sola.opentenure.services.boundary.beans.claim;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.sola.common.ClaimStatusConstants;
import org.sola.common.ConfigConstants;
import org.sola.common.DateUtility;
import org.sola.common.FileUtility;
import org.sola.common.RolesConstants;
import org.sola.common.StringUtility;
import org.sola.common.mapping.MappingManager;
import org.sola.cs.services.ejb.refdata.entities.GenderType;
import org.sola.cs.services.ejb.refdata.entities.IdType;
import org.sola.cs.services.ejb.refdata.entities.LandUseType;
import org.sola.cs.services.ejb.refdata.entities.RejectionReason;
import org.sola.cs.services.ejb.refdata.entities.RrrType;
import org.sola.cs.services.ejb.system.businesslogic.SystemCSEJBLocal;
import org.sola.cs.services.ejbs.admin.businesslogic.AdminCSEJBLocal;
import org.sola.cs.services.ejbs.claim.businesslogic.ClaimEJBLocal;
import org.sola.cs.services.ejbs.claim.entities.Attachment;
import org.sola.cs.services.ejbs.claim.entities.Claim;
import org.sola.cs.services.ejbs.claim.entities.ClaimComment;
import org.sola.cs.services.ejbs.claim.entities.ClaimLocation;
import org.sola.cs.services.ejbs.claim.entities.ClaimParty;
import org.sola.cs.services.ejbs.claim.entities.ClaimPermissions;
import org.sola.cs.services.ejbs.claim.entities.ClaimShare;
import org.sola.opentenure.services.boundary.beans.AbstractBackingBean;
import org.sola.opentenure.services.boundary.beans.exceptions.OTWebException;
import org.sola.opentenure.services.boundary.beans.features.AdditionalLocationFeature;
import org.sola.opentenure.services.boundary.beans.helpers.DateBean;
import org.sola.opentenure.services.boundary.beans.helpers.ErrorKeys;
import org.sola.opentenure.services.boundary.beans.helpers.MessageBean;
import org.sola.opentenure.services.boundary.beans.helpers.MessageProvider;
import org.sola.opentenure.services.boundary.beans.helpers.MessagesKeys;
import org.sola.opentenure.services.boundary.beans.language.LanguageBean;
import org.sola.opentenure.services.boundary.beans.referencedata.ReferenceData;
import org.sola.services.common.EntityAction;
import org.sola.services.common.LocalInfo;
import org.sola.services.common.logging.LogUtility;

/**
 * Provides method and listeners for claim page
 */
@Named
@ViewScoped
public class ClaimPageBean extends AbstractBackingBean {

    @EJB
    ClaimEJBLocal claimEjb;

    @EJB
    AdminCSEJBLocal adminEjb;

    @EJB
    SystemCSEJBLocal systemEjb;

    @Inject
    ReferenceData refData;

    @Inject
    MessageProvider msgProvider;

    @Inject
    MessageBean msg;

    @Inject
    DateBean dateBean;

    @Inject
    DynaFormBean dynaFormBean;

    @Inject
    LanguageBean langBean;

    private Claim claim;
    private String id;
    private Claim challengedClaim = null;
    private String challengedClaimNr;
    private String statusName;
    private String landUseName;
    private String claimTypeName;
    private String recorderName;
    private Claim[] challengingClaims = null;
    private String assigneeName = null;
    private ClaimPermissions claimPermissions;
    private final String ACTION_ASSIGNED = "assigned";
    private final String ACTION_UNASSIGNED = "unassigned";
    private final String ACTION_REJECTED = "rejected";
    private final String ACTION_WITHDRAWN = "withdrawn";
    private final String ACTION_REVIEW_APPROVED = "reviewed";
    private final String ACTION_REVIEW_REVERTED = "reverted";
    private final String ACTION_MODERATION_APPROVED = "moderated";
    private final String ACTION_SUBMITTED = "submitted";
    private final String ACTION_ISSUED = "issued";
    private final String ACTION_SAVED = "saved";
    private final String ACTION_TRANSFER = "transfer";
    private final String ACTION_TRANSFERRED = "transferred";
    private String rejectionReasonCode;
    private String commentText;
    private String commentId;
    private ClaimParty party;
    private ClaimShare share;
    private String shareId;
    private boolean canViewFullInfo = false;
    private String claimAdditionalLocationFeatures;
    private String challengedClaimId;
    private String challengeExpiryTime;
    private String challengeExpiryDate;
    private boolean isTransfer = false;

    public ClaimPageBean() {
        super();
    }

    @PostConstruct
    private void init() {
        id = getRequestParam("id");
        challengedClaimId = getRequestParam("challengedId");
        String action = getRequestParam("action");

        if (!StringUtility.isEmpty(action)) {
            if (action.equalsIgnoreCase(ACTION_TRANSFER)) {
                isTransfer = true;
            }
            if (action.equalsIgnoreCase(ACTION_TRANSFERRED)) {
                msg.setSuccessMessage(msgProvider.getMessage(MessagesKeys.CLAIM_PAGE_RIGHTS_TRANSFERRED));
            }
            if (action.equalsIgnoreCase(ACTION_ASSIGNED)) {
                msg.setSuccessMessage(msgProvider.getMessage(MessagesKeys.CLAIM_PAGE_ASSIGNED_TO_YOU));
            }
            if (action.equalsIgnoreCase(ACTION_UNASSIGNED)) {
                msg.setSuccessMessage(msgProvider.getMessage(MessagesKeys.CLAIM_PAGE_UNASSIGNED));
            }
            if (action.equalsIgnoreCase(ACTION_WITHDRAWN)) {
                msg.setSuccessMessage(msgProvider.getMessage(MessagesKeys.CLAIM_PAGE_WITHDRAWN));
            }
            if (action.equalsIgnoreCase(ACTION_REJECTED)) {
                msg.setSuccessMessage(msgProvider.getMessage(MessagesKeys.CLAIM_PAGE_REJECTED));
            }
            if (action.equalsIgnoreCase(ACTION_REVIEW_APPROVED)) {
                msg.setSuccessMessage(msgProvider.getMessage(MessagesKeys.CLAIM_PAGE_APPROVED_FOR_REVIEW));
            }
            if (action.equalsIgnoreCase(ACTION_MODERATION_APPROVED)) {
                msg.setSuccessMessage(msgProvider.getMessage(MessagesKeys.CLAIM_PAGE_APPROVED_MODERATION));
            }
            if (action.equalsIgnoreCase(ACTION_SAVED)) {
                msg.setSuccessMessage(msgProvider.getMessage(MessagesKeys.CLAIM_PAGE_SAVED));
            }
            if (action.equalsIgnoreCase(ACTION_SUBMITTED)) {
                msg.setSuccessMessage(msgProvider.getMessage(MessagesKeys.CLAIM_PAGE_SUBMITTED));
            }
            if (action.equalsIgnoreCase(ACTION_REVIEW_REVERTED)) {
                msg.setSuccessMessage(msgProvider.getMessage(MessagesKeys.CLAIM_PAGE_REVERTED));
            }
            if (action.equalsIgnoreCase(ACTION_ISSUED)) {
                msg.setSuccessMessage(msgProvider.getMessage(MessagesKeys.CLAIM_PAGE_ISSUED));
            }
        }
        loadClaim();
        dynaFormBean.init(claim);
    }

    private ClaimPermissions getClaimPermissions() {
        if (claimPermissions == null) {
            // If claim is new, check user role to have recorder rights and create permissions manually
            if (claim.isNew()) {
                claimPermissions = new ClaimPermissions();
                claimPermissions.setCanEdit(isInRole(RolesConstants.CS_RECORD_CLAIM));
            } else {
                claimPermissions = claimEjb.getClaimPermissions(id);
            }
        }
        return claimPermissions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean getIsGeometryRequired() {
        String requireSpatial = systemEjb.getSetting(ConfigConstants.REQUIRES_SPATIAL, "1");
        return !getIsSubmitted() && requireSpatial.equals("1");
    }

    public boolean getIsSubmitted() {
        return claim == null || claim.getStatusCode() == null
                || claim.getStatusCode().equalsIgnoreCase(ClaimStatusConstants.CREATED);
    }

    public String getClaimAdditionalLocationFeatures() {
        return claimAdditionalLocationFeatures;
    }

    public void setClaimAdditionalLocationFeatures(String claimAdditionalLocationFeatures) {
        this.claimAdditionalLocationFeatures = claimAdditionalLocationFeatures;
    }

    public ClaimParty getParty() {
        if (party == null) {
            party = new ClaimParty();
            party.setId(UUID.randomUUID().toString());
            party.setEntityAction(EntityAction.INSERT);
            party.setPerson(true);
        }
        return party;
    }

    public String getChallengeExpiryTime() {
        return challengeExpiryTime;
    }

    public void setChallengeExpiryTime(String time) {
        challengeExpiryTime = time;
    }

    public String getChallengeExpiryDate() {
        return challengeExpiryDate;
    }

    public void setChallengeExpiryDate(String date) {
        challengeExpiryDate = date;
    }

    public String getStartDate() {
        return dateBean.getShortDate(claim.getStartDate());
    }

    public void setStartDate(String startDate) {
        if (!StringUtility.isEmpty(startDate)) {
            claim.setStartDate(DateUtility.convertToDate(startDate, dateBean.getDatePattern()));
        } else {
            claim.setStartDate(null);
        }
    }

    public String getPartyBirthday() {
        return dateBean.getShortDate(party.getBirthDate());
    }

    public void setPartyBirthday(String birthday) {
        if (!StringUtility.isEmpty(birthday)) {
            party.setBirthDate(DateUtility.convertToDate(birthday, dateBean.getDatePattern()));
        } else {
            party.setBirthDate(null);
        }
    }

    public String getChallengedClaimNr() {
        return challengedClaimNr;
    }

    public void setChallengedClaimNr(String challengedClaimNr) {
        this.challengedClaimNr = challengedClaimNr;
    }

    public String getRejectionReasonCode() {
        return rejectionReasonCode;
    }

    public void setRejectionReasonCode(String rejectionReason) {
        this.rejectionReasonCode = rejectionReason;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getAssigneeName() {
        if (assigneeName == null && claim != null && claim.getAssigneeName() != null) {
            assigneeName = getFullUserName(claim.getAssigneeName());
        }
        return assigneeName;
    }

    public String getFullUserName(String userName) {
        if (!StringUtility.isEmpty(userName)) {
            return adminEjb.getUserFullName(userName);
        }
        return "";
    }

    public String getHeader() {
        String number;
        if (!StringUtility.isEmpty(claim.getNr())) {
            number = claim.getNr();
        } else {
            number = msgProvider.getMessage(MessagesKeys.GENERAL_LABEL_NEW);
        }

        if (getIsClaimChallenge()) {
            return String.format(msgProvider.getMessage(MessagesKeys.CLAIM_PAGE_CHALLENGE_TITLE), number);
        } else {
            return String.format(msgProvider.getMessage(MessagesKeys.CLAIM_PAGE_TITLE), number);
        }
    }

    public boolean getIsClaimChallenge() {
        return claim != null && !StringUtility.isEmpty(claim.getChallengedClaimId());
    }

    public String getStatusName() {
        if (StringUtility.isEmpty(statusName) && claim != null) {
            statusName = refData.getBeanDisplayValue(refData.getClaimStatuses(langBean.getLocale()), claim.getStatusCode());
        }
        return statusName;
    }

    public String getLandUseName() {
        if (StringUtility.isEmpty(landUseName) && claim != null) {
            landUseName = refData.getBeanDisplayValue(refData.getLandUses(langBean.getLocale(), false), claim.getLandUseCode());
        }
        return landUseName;
    }

    public String getClaimType() {
        if (StringUtility.isEmpty(claimTypeName) && claim != null) {
            claimTypeName = refData.getBeanDisplayValue(refData.getRightTypes(langBean.getLocale(), false), claim.getTypeCode());
        }
        return claimTypeName;
    }

    public boolean getIsOnPublicDisplay() {
        if (claim.getStatusCode() != null && claim.getStatusCode().equalsIgnoreCase(ClaimStatusConstants.UNMODERATED)
                && claim.getChallengeExpiryDate() != null && claim.getChallengeExpiryDate().after(Calendar.getInstance().getTime())) {
            return true;
        } else {
            return false;
        }
    }

    public String getRejectionReason() {
        if (claim != null && !StringUtility.isEmpty(claim.getRejectionReasonCode())) {
            return refData.getBeanDisplayValue(refData.getRejectionReasons(langBean.getLocale(), false), claim.getRejectionReasonCode());
        }
        return "";
    }

    public RrrType[] getClaimTypes() {
        return refData.getRightTypes(true, langBean.getLocale(), true);
    }

    public LandUseType[] getLandUses() {
        return refData.getLandUses(true, langBean.getLocale(), true);
    }

    public RejectionReason[] getRejectionReasons() {
        return refData.getRejectionReasons(false, langBean.getLocale(), true);
    }

    public GenderType[] getGenderTypes() {
        return refData.getGenderTypes(party.isNew(), langBean.getLocale(), true);
    }

    public IdType[] getIdTypes() {
        return refData.getIdTypes(StringUtility.isEmpty(party.getIdTypeCode()), langBean.getLocale(), true);
    }

    public String getGenderName() {
        if (claim != null && claim.getClaimant() != null) {
            return refData.getBeanDisplayValue(refData.getGenderTypes(langBean.getLocale(), false), claim.getClaimant().getGenderCode());
        }
        return "";
    }

    public String getGenderName(String code) {
        if (!StringUtility.isEmpty(code)) {
            return refData.getBeanDisplayValue(refData.getGenderTypes(langBean.getLocale(), false), code);
        }
        return "";
    }

    public String getIdTypeName(String code) {
        if (!StringUtility.isEmpty(code)) {
            return refData.getBeanDisplayValue(refData.getIdTypes(langBean.getLocale(), false), code);
        }
        return "";
    }

    public String getIdTypeName() {
        if (claim != null && claim.getClaimant() != null) {
            return refData.getBeanDisplayValue(refData.getIdTypes(langBean.getLocale(), false), claim.getClaimant().getIdTypeCode());
        }
        return "";
    }

    public String getDocTypeName(String code) {
        return refData.getBeanDisplayValue(refData.getDocumentTypes(langBean.getLocale(), false), code);
    }

    public String getRecorderName() {
        if (StringUtility.isEmpty(recorderName) && claim != null) {
            recorderName = getFullUserName(claim.getRecorderName());
        }
        return recorderName;
    }

    public boolean isCanViewFullInfo() {
        return canViewFullInfo;
    }

    public Claim[] getChallengingClaims() {
        if (challengingClaims == null && claim != null) {
            List<Claim> claims = claimEjb.getChallengingClaimsByChallengedId(claim.getId());
            if (claims != null) {
                // Remove claims with CREATED status
                for (Iterator<Claim> it = claims.iterator(); it.hasNext();) {
                    Claim challenge = it.next();
                    if (challenge.getStatusCode().equalsIgnoreCase(ClaimStatusConstants.CREATED)
                            && !challenge.getRecorderName().equalsIgnoreCase(getUserName())) {
                        it.remove();
                    }
                }
                challengingClaims = claims.toArray(new Claim[claims.size()]);
            }
        }
        return challengingClaims;
    }

    public Attachment[] getAttachments() {
        if (claim != null && claim.getAttachments() != null && claim.getAttachments().size() > 0) {
            List<Attachment> attachments = new ArrayList<>();

            for (Attachment attachment : claim.getAttachments()) {
                if (attachment.getEntityAction() == null || (!attachment.getEntityAction().equals(EntityAction.DELETE))) {
                    attachments.add(attachment);
                }
            }
            return attachments.toArray(new Attachment[attachments.size()]);
        }
        return null;
    }

    public ClaimComment[] getComments() {
        if (claim != null && claim.getComments() != null && claim.getComments().size() > 0) {
            List<ClaimComment> comments = new ArrayList<>();

            for (ClaimComment comment : claim.getComments()) {
                if (comment.getEntityAction() == null || (!comment.getEntityAction().equals(EntityAction.DELETE))) {
                    comments.add(comment);
                }
            }
            return comments.toArray(new ClaimComment[comments.size()]);
        }
        return null;
    }

    // Display history label for the first record only
    public boolean isToDisplaySharesHistory(ClaimShare claimShare) {
        if (isShareHistoric(claimShare)) {
            for (int i = 0; i <= claim.getShares().size(); i++) {
                if (isShareHistoric(claim.getShares().get(i))) {
                    if (!StringUtility.isEmpty(claimShare.getId())
                            && !StringUtility.isEmpty(claim.getShares().get(i).getId())
                            && claimShare.getId().equals(claim.getShares().get(i).getId())) {
                        return true;
                    }
                    return false;
                }
            }
        }
        return false;
    }

    public boolean isShareActive(ClaimShare claimShare) {
        return StringUtility.empty(claimShare.getStatus()).equalsIgnoreCase(ClaimShare.STATUS_ACTIVE) || StringUtility.isEmpty(claimShare.getStatus());
    }

    public boolean isShareHistoric(ClaimShare claimShare) {
        return StringUtility.empty(claimShare.getStatus()).equalsIgnoreCase(ClaimShare.STATUS_HISTORIC);
    }

    public ClaimShare[] getClaimShares() {
        if (claim != null && claim.getShares() != null && claim.getShares().size() > 0) {
            List<ClaimShare> shares = new ArrayList<>();

            for (ClaimShare claimShare : claim.getShares()) {
                if (claimShare.getEntityAction() == null || (!claimShare.getEntityAction().equals(EntityAction.DELETE))) {
                    shares.add(claimShare);
                }
            }
            return shares.toArray(new ClaimShare[shares.size()]);
        }
        return null;
    }

    public ClaimParty[] getOwners(List<ClaimParty> owners) {
        if (owners != null && owners.size() > 0) {
            List<ClaimParty> claimOwners = new ArrayList<>();
            for (ClaimParty owner : owners) {
                if (owner.getEntityAction() == null || (!owner.getEntityAction().equals(EntityAction.DELETE))) {
                    claimOwners.add(owner);
                }
            }
            return claimOwners.toArray(new ClaimParty[claimOwners.size()]);
        }
        return null;
    }

    public Claim getChallengedClaim() {
        if (challengedClaim == null && claim != null && !StringUtility.isEmpty(claim.getChallengedClaimId())) {
            challengedClaim = claimEjb.getClaim(claim.getChallengedClaimId());
        }
        return challengedClaim;
    }

    public boolean getCanIssueCertificate() {
        return getClaimPermissions().isCanIssue();
    }

    public boolean getCanPrintCertificate() {
        return getClaimPermissions().isCanPrintCertificate();
    }

    public boolean getCanDelete() {
        return getClaimPermissions().isCanDelete();
    }

    public boolean getCanAssign() {
        return getClaimPermissions().isCanAssign();
    }

    public boolean getCanSubmit() {
        return getClaimPermissions().isCanSubmitClaim();
    }

    public boolean getCanAddDocuments() {
        return getClaimPermissions().isCanAddDocumentsToClaim();
    }

    public boolean getCanChallenge() {
        return getClaimPermissions().isCanChallengeClaim();
    }

    public boolean getCanUnAssign() {
        return getClaimPermissions().isCanUnAssign();
    }

    public boolean getCanApproveReview() {
        return getClaimPermissions().isCanApproveReview();
    }

    public boolean getCanRevertReview() {
        return getClaimPermissions().isCanRevert();
    }

    public boolean getCanApproveModeration() {
        return getClaimPermissions().isCanApproveModeration();
    }

    public boolean getCanReject() {
        return getClaimPermissions().isCanReject();
    }

    public boolean getCanWithdraw() {
        return getClaimPermissions().isCanWithdraw();
    }

    public boolean getCanEdit() {
        return getClaimPermissions().isCanEdit();
    }

    public void checkCanEdit() throws Exception {
        if (!getClaimPermissions().isCanEdit()) {
            throw new OTWebException(msgProvider.getErrorMessage(ErrorKeys.CLAIM_EDIT_NOT_ALLOWED));
        }
    }

    public void checkCanTransfer() throws Exception {
        if (!getClaimPermissions().isCanTransfer()) {
            throw new OTWebException(msgProvider.getErrorMessage(ErrorKeys.CLAIM_TRANSFER_NOT_ALLOWED));
        }
    }

    public boolean getCanTransfer() {
        return getClaimPermissions().isCanTransfer();
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public boolean getAllowChallengeExpiryDateChange() {
        if (claim.getChallengeExpiryDate() != null && claim.getStatusCode() != null) {
            // Allow change of expiration date only for unmoderated claims
            if (!claim.getStatusCode().equalsIgnoreCase(ClaimStatusConstants.UNMODERATED)) {
                return false;
            } else {
                // Allow change of expiration date only for unmoderated claims and users with Reviewer/Moderator roles
                if (!adminEjb.isInRole(RolesConstants.CS_REVIEW_CLAIM, RolesConstants.CS_MODERATE_CLAIM)) {
                    return false;
                }
                // Don't allow change of expiration date if it's already expired
                if (claim.getChallengeExpiryDate().before(Calendar.getInstance().getTime())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private void loadClaim() {
        if (!StringUtility.isEmpty(id)) {
            if (claim == null) {
                claim = claimEjb.getClaim(id);
                if (claim == null
                        || (claim.getStatusCode().equalsIgnoreCase(ClaimStatusConstants.CREATED)
                        && !claim.getRecorderName().equalsIgnoreCase(getUserName()))) {
                    throw new FacesException(msgProvider.getErrorMessage(ErrorKeys.CLAIM_NOT_FOUND));
                    //claim = new Claim();
                    //getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_NOT_FOUND)));
                } else {
                    challengeExpiryTime = dateBean.getTime(claim.getChallengeExpiryDate());
                    challengeExpiryDate = dateBean.getShortDate(claim.getChallengeExpiryDate());

                    // If transfer transaction, mark existing shares with historic status and remove documents and comments for not displaying them
                    if (isTransfer) {
                        if (claim.getShares() != null) {
                            for (ClaimShare claimShare : claim.getShares()) {
                                if (!StringUtility.empty(claimShare.getStatus()).equalsIgnoreCase(ClaimShare.STATUS_HISTORIC)) {
                                    claimShare.setStatus(ClaimShare.STATUS_HISTORIC);
                                }
                            }
                        }

                        if (claim.getAttachments() != null) {
                            claim.getAttachments().clear();
                        }

                        if (claim.getComments() != null) {
                            claim.getComments().clear();
                        }
                    }
                }
            }
        } else {
            if (isTransfer) {
                throw new FacesException(msgProvider.getErrorMessage(ErrorKeys.CLAIM_NOT_FOUND));
            }

            // Create new claim
            claim = new Claim();
            claim.setId(UUID.randomUUID().toString());
            claim.setClaimArea(0L);

            // Check if claim is challenge
            if (!StringUtility.isEmpty(challengedClaimId)) {
                // Get challenged claim
                challengedClaim = claimEjb.getClaim(challengedClaimId);

                if (challengedClaim != null) {

                    challengedClaimNr = challengedClaim.getNr();

                    // Copy over claim properties
                    claim.setChallengedClaimId(challengedClaim.getId());
                    claim.setMappedGeometry(challengedClaim.getMappedGeometry());

                    if (challengedClaim.getLocations() != null && challengedClaim.getLocations().size() > 0) {
                        claim.setLocations(new ArrayList<ClaimLocation>());
                        for (ClaimLocation location : challengedClaim.getLocations()) {
                            ClaimLocation newLocation = MappingManager.getMapper().map(location, ClaimLocation.class);
                            newLocation.setId(UUID.randomUUID().toString());
                            newLocation.setRowId(null);
                            newLocation.setRowVersion(0);
                            newLocation.setLoaded(false);
                            claim.getLocations().add(newLocation);
                        }
                    }

                    claim.setNotes(challengedClaim.getNotes());
                    claim.setNorthAdjacency(challengedClaim.getNorthAdjacency());
                    claim.setSouthAdjacency(challengedClaim.getSouthAdjacency());
                    claim.setWestAdjacency(challengedClaim.getWestAdjacency());
                    claim.setEastAdjacency(challengedClaim.getEastAdjacency());
                }
            }
        }

        // Check if sensitive information can be displyed
        if (claim.isNew() || isInRole(RolesConstants.CS_MODERATE_CLAIM)
                || isInRole(RolesConstants.CS_REVIEW_CLAIM)
                || (isInRole(RolesConstants.CS_RECORD_CLAIM)
                && StringUtility.empty(claim.getRecorderName()).equalsIgnoreCase(getUserName()))) {
            canViewFullInfo = true;
        } else {
            canViewFullInfo = false;
        }
    }

    /**
     * Indicates whether claim page bean is loaded for transfer transaction
     */
    public boolean getIsTransfer() {
        return isTransfer;
    }

    public void issueCertificate() {
        try {
            if (!StringUtility.isEmpty(id) && getCanIssueCertificate()) {
                runUpdate(new Runnable() {
                    @Override
                    public void run() {
                        LocalInfo.setBaseUrl(getApplicationUrl());
                        if (claimEjb.issueClaim(id, langBean.getLocale())) {
                            redirectWithAction(ACTION_ISSUED);
                        }
                    }
                });
            }
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }

    public void deleteClaim() {
        try {
            if (!StringUtility.isEmpty(id) && getCanDelete()) {
                runUpdate(new Runnable() {
                    @Override
                    public void run() {
                        LocalInfo.setBaseUrl(getApplicationUrl());
                        claimEjb.deleteClaim(id);
                    }
                });
            }
            getContext().getExternalContext().redirect(getRequest().getContextPath() + "/Dashboard.xhtml");
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }

    public void assignClaim() {
        try {
            if (!StringUtility.isEmpty(id) && getCanAssign()) {
                runUpdate(new Runnable() {
                    @Override
                    public void run() {
                        LocalInfo.setBaseUrl(getApplicationUrl());
                        claimEjb.assignClaim(id);
                    }
                });
                redirectWithAction(ACTION_ASSIGNED);
            }
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }

    public void unAssignClaim() {
        try {
            if (!StringUtility.isEmpty(id) && getCanUnAssign()) {
                runUpdate(new Runnable() {
                    @Override
                    public void run() {
                        LocalInfo.setBaseUrl(getApplicationUrl());
                        claimEjb.unAssignClaim(id);
                    }
                });
                redirectWithAction(ACTION_UNASSIGNED);
            }
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }

    public void saveComment(AjaxBehaviorEvent event) {
        try {
            if (StringUtility.isEmpty(commentText)) {
                return;
            }
            if (!StringUtility.isEmpty(commentId)) {
                // search for id
                for (ClaimComment comment : claim.getComments()) {
                    if (comment.getId().equalsIgnoreCase(commentId)) {
                        comment.setComment(commentText);
                        break;
                    }
                }
            } else {
                if (claim.getComments() == null) {
                    claim.setComments(new ArrayList<ClaimComment>());
                }
                ClaimComment comment = new ClaimComment();
                comment.setId(UUID.randomUUID().toString());
                comment.setClaimId(claim.getId());
                comment.setComment(commentText);
                comment.setCommentUser(getUserName());
                claim.getComments().add(comment);
            }

            setCommentText("");
            setCommentId("");
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }

    public void rejectClaim() {
        try {
            if (!StringUtility.isEmpty(id) && getCanReject()) {
                // Check rejection code
                if (StringUtility.isEmpty(rejectionReasonCode)) {
                    throw new Exception(msgProvider.getErrorMessage(ErrorKeys.CLAIM_REJECTION_REASON_REQUIRED) + "\r\n");
                }

                runUpdate(new Runnable() {
                    @Override
                    public void run() {
                        LocalInfo.setBaseUrl(getApplicationUrl());
                        claimEjb.rejectClaim(id, rejectionReasonCode);
                    }
                });
                redirectWithAction(ACTION_REJECTED);
            }
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }

    public void saveClaim() {
        try {
            if (!getCanEdit() || !validateClaim()) {
                return;
            }

            // Assign challenged claim ID if number is provided
            if (claim.isNew() && !StringUtility.isEmpty(challengedClaimNr)) {
                Claim challengedClaimTmp = claimEjb.getClaimByNumber(challengedClaimNr);
                // Restrict returning claims with CREATED status if current user is not the owner of the claim
                if (challengedClaimTmp != null
                        && !StringUtility.empty(challengedClaimTmp.getStatusCode()).equalsIgnoreCase(ClaimStatusConstants.CREATED)) {
                    claim.setChallengedClaimId(challengedClaimTmp.getId());
                }
            }

            // Set challenge expiration date 
            if (getAllowChallengeExpiryDateChange()) {
                DateFormat format = new SimpleDateFormat(dateBean.getDateFormatForDisplay() + " kk:mm");
                claim.setChallengeExpiryDate(format.parse(challengeExpiryDate + " " + challengeExpiryTime));
            }

            runUpdate(new Runnable() {
                @Override
                public void run() {
                    LocalInfo.setBaseUrl(getApplicationUrl());
                    claim = claimEjb.saveClaim(claim, langBean.getLocale());
                }
            });
            redirectWithAction(ACTION_SAVED);
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }

    public void makeTransfer() {
        try {
            if (!validateShares(true)) {
                return;
            }

            // Save transfer
            runUpdate(new Runnable() {
                @Override
                public void run() {
                    LocalInfo.setBaseUrl(getApplicationUrl());
                    claim = claimEjb.transferClaim(claim, langBean.getLocale());
                }
            });
            redirectWithAction(ACTION_TRANSFERRED);

        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }

    private boolean validateClaim() throws Exception {
        boolean isValid = true;
        boolean fullValidation = !getIsSubmitted();

        // Type
        if (fullValidation && StringUtility.isEmpty(claim.getTypeCode())) {
            isValid = false;
            getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_TYPE_REQUIRED)));
        }
        // Land use
        if (fullValidation && StringUtility.isEmpty(claim.getLandUseCode())) {
            isValid = false;
            getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_LAND_USE_REQUIRED)));
        }
        // Claimant
        if (claim.getClaimant() == null) {
            isValid = false;
            getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_CLAIMANT_REQUIRED)));
        } else if (!validateParty(claim.getClaimant(), true, false)) {
            isValid = false;
        }
        // Challenge expiry date
        if (getAllowChallengeExpiryDateChange()) {
            if (StringUtility.isEmpty(challengeExpiryDate) || StringUtility.isEmpty(challengeExpiryTime)) {
                isValid = false;
                getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_CHALLENGE_EXPIRATION_DATE_REQUIRED)));
            }
        }

        // Mapped geometry
        String requireSpatial = systemEjb.getSetting(ConfigConstants.REQUIRES_SPATIAL, "1");

        if (fullValidation && requireSpatial.equals("1")
                && StringUtility.isEmpty(claim.getMappedGeometry())) {
            isValid = false;
            getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_MAPPED_GEOMETRY_REQUIRED)));
        }

        // Handle claim additional locations
        AdditionalLocationFeature[] claimLocations = {};
        if (!StringUtility.isEmpty(claimAdditionalLocationFeatures)) {
            claimLocations = getMapper().readValue(claimAdditionalLocationFeatures, AdditionalLocationFeature[].class);
        }

        // Update and delete locations
        if (claim.getLocations() != null && claim.getLocations().size() > 0) {
            boolean found;
            for (ClaimLocation location : claim.getLocations()) {
                found = false;
                for (AdditionalLocationFeature feature : claimLocations) {
                    // Update exiting 
                    if (location.getId().equalsIgnoreCase(feature.getId())) {
                        // Check required fields
                        if (StringUtility.isEmpty(feature.getDescription())) {
                            getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.MAP_CONTROL_CLAIM_ADDITIONAL_LOCATION_DESCRIPTION_REQUIRED)));
                            isValid = false;
                        } else {
                            location.setMappedLocation(feature.getGeometry());
                            location.setDescription(feature.getDescription());
                        }
                        found = true;
                        break;
                    }
                }
                // Delete not found items
                if (!found) {
                    location.setEntityAction(EntityAction.DELETE);
                }
            }
        }

        // Add new locations
        if (claimLocations.length > 0) {
            boolean found;
            if (claim.getLocations() == null) {
                claim.setLocations(new ArrayList<ClaimLocation>());
            }
            for (AdditionalLocationFeature feature : claimLocations) {
                found = false;
                for (ClaimLocation location : claim.getLocations()) {
                    if (location.getId().equalsIgnoreCase(feature.getId())) {
                        found = true;
                        break;
                    }
                }
                // Add new
                if (!found) {
                    // Check required fields
                    if (StringUtility.isEmpty(feature.getDescription())) {
                        getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.MAP_CONTROL_CLAIM_ADDITIONAL_LOCATION_DESCRIPTION_REQUIRED)));
                        isValid = false;
                    } else {
                        ClaimLocation location = new ClaimLocation();
                        location.setId(UUID.randomUUID().toString());
                        location.setMappedLocation(feature.getGeometry());
                        location.setDescription(feature.getDescription());
                        claim.getLocations().add(location);
                    }
                }
            }
        }

        // If challenged claim is provided, check it exists
        if (claim.isNew() && !StringUtility.isEmpty(challengedClaimNr)) {
            Claim challengedClaimTmp = claimEjb.getClaimByNumber(challengedClaimNr);
            if (challengedClaimTmp == null) {
                getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_CHALLENGED_CLAIM_NOT_FOUND)));
                isValid = false;
            } else // Check challenged claim status
            {
                if (!challengedClaimTmp.getStatusCode().equalsIgnoreCase(ClaimStatusConstants.UNMODERATED)
                        || challengedClaimTmp.getChallengeExpiryDate().before(Calendar.getInstance().getTime())
                        || !StringUtility.isEmpty(challengedClaimTmp.getChallengedClaimId())) {
                    getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_CHALLENGED_CLAIM_CANT_CHALLENGED)));
                    isValid = false;
                }
            }
        }

        // Check shares
        isValid = validateShares(fullValidation);

        return isValid;
    }

    private boolean validateShares(boolean fullValidation) {
        if (fullValidation && (claim.getShares() == null || getEntityListSize(getActiveShares()) < 1)) {
            getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_SHARES_REQUIRED)));
            return false;
        }

        if (claim.getShares() != null) {
            double totalShare = 0;

            for (ClaimShare claimShare : getActiveShares()) {
                if (claimShare.getEntityAction() == null || !claimShare.getEntityAction().equals(EntityAction.DELETE)) {
                    totalShare += (double) claimShare.getPercentage();

                    try {
                        if (!validateShare(claimShare, false)) {
                            return false;
                        }
                        if (claimShare.getOwners() == null || getEntityListSize(claimShare.getOwners()) < 1) {
                            getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_SHARE_OWNER_REQUIRED)));
                            return false;
                        } else {
                            // Validate owners
                            for (ClaimParty claimParty : claimShare.getOwners()) {
                                if (claimParty.getEntityAction() == null || !claimParty.getEntityAction().equals(EntityAction.DELETE)) {
                                    if (!validateParty(claimParty, false, false)) {
                                        return false;
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        return false;
                    }
                }
            }

            if (totalShare - 100 > 0.01 || totalShare - 100 < -0.01) {
                getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_SHARE_TOTAL_SHARE_WRONG)));
                return false;
            }
        }
        return true;
    }

    public List<ClaimShare> getActiveShares() {
        ArrayList<ClaimShare> shares = new ArrayList<>();
        if (claim.getShares() != null) {
            for (ClaimShare claimShare : claim.getShares()) {
                if (isShareActive(claimShare)) {
                    shares.add(claimShare);
                }
            }
        }
        return shares;
    }

    private boolean validateParty(ClaimParty party, boolean isClaimant, boolean throwException) throws Exception {
        if (party == null) {
            return false;
        }

        boolean isValid = true;
        String errors = "";

        if (StringUtility.isEmpty(party.getName())) {
            if (isClaimant) {
                if (throwException) {
                    errors += "- " + msgProvider.getErrorMessage(ErrorKeys.CLAIM_CLAIMANT_NAME_REQUIRED) + "\r\n";
                } else {
                    getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_CLAIMANT_NAME_REQUIRED)));
                }
            } else if (throwException) {
                errors += "- " + msgProvider.getErrorMessage(ErrorKeys.CLAIM_OWNER_NAME_REQUIRED) + "\r\n";
            } else {
                getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_OWNER_NAME_REQUIRED)));
            }
            isValid = false;
        }
        if (throwException && !StringUtility.isEmpty(errors)) {
            throw new Exception(errors);
        }

        // Override empty values with NULL. This is the bug of JSF ignoring INTERPRET_EMPTY_STRING_SUBMITTED_VALUES_AS_NULL
        if (isValid) {
            if (StringUtility.isEmpty(party.getGenderCode())) {
                party.setGenderCode(null);
            }
            if (StringUtility.isEmpty(party.getIdTypeCode())) {
                party.setIdTypeCode(null);
            }
        }
        return isValid;
    }

    public void addClaimant(boolean isPerson) {
        if (claim.getClaimant() != null) {
            loadClaimant();
        } else {
            party = new ClaimParty();
            party.setId(UUID.randomUUID().toString());
            party.setEntityAction(EntityAction.INSERT);
            party.setPerson(isPerson);
        }
    }

    public void loadClaimant() {
        // Make a copy of claimant
        if (claim.getClaimant() != null) {
            MappingManager.getMapper().map(claim.getClaimant(), party);
        } else {
            addClaimant(true);
        }
    }

    public void saveClaimant() throws Exception {
        if (validateParty(party, true, true)) {
            if (claim.getClaimant() == null) {
                claim.setClaimant(MappingManager.getMapper().map(party, ClaimParty.class));
            } else {
                MappingManager.getMapper().map(party, claim.getClaimant());
            }
        }
    }

    public void addOwner(boolean isPerson, String shareId) {
        party = new ClaimParty();
        party.setEntityAction(EntityAction.INSERT);
        party.setId(UUID.randomUUID().toString());
        party.setPerson(isPerson);
        this.shareId = shareId;
    }

    public void copyClaimant(String shareId) {
        if (claim.getClaimant() != null && !StringUtility.isEmpty(claim.getClaimant().getName())) {
            ClaimParty owner = MappingManager.getMapper().map(claim.getClaimant(), ClaimParty.class);
            owner.setId(UUID.randomUUID().toString());
            owner.setRowId(null);
            owner.setRowVersion(0);
            owner.setLoaded(false);
            addOwner(owner, shareId);
        }
    }

    public void loadOwner(String shareId, String ownerId) {
        // Make a copy of owner
        this.shareId = null;
        if (claim.getShares() != null) {
            for (ClaimShare claimShare : claim.getShares()) {
                if (claimShare.getId().equalsIgnoreCase(shareId) && (claimShare.getEntityAction() == null || !claimShare.getEntityAction().equals(EntityAction.DELETE))) {
                    if (claimShare.getOwners() != null) {
                        for (ClaimParty claimParty : claimShare.getOwners()) {
                            if (claimParty.getId().equalsIgnoreCase(ownerId) && (claimParty.getEntityAction() == null || !claimParty.getEntityAction().equals(EntityAction.DELETE))) {
                                MappingManager.getMapper().map(claimParty, party);
                                break;
                            }
                        }
                    }
                    this.shareId = shareId;
                    break;
                }
            }
        }
    }

    public void deleteOwner(String shareId, String ownerId) {
        if (claim.getShares() != null) {
            for (ClaimShare claimShare : claim.getShares()) {
                if (claimShare.getId().equalsIgnoreCase(shareId) && (claimShare.getEntityAction() == null || !claimShare.getEntityAction().equals(EntityAction.DELETE))) {
                    if (claimShare.getOwners() != null) {
                        for (ClaimParty claimParty : claimShare.getOwners()) {
                            if (claimParty.getId().equalsIgnoreCase(ownerId) && (claimParty.getEntityAction() == null || !claimParty.getEntityAction().equals(EntityAction.DELETE))) {
                                claimParty.setEntityAction(EntityAction.DELETE);
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }
    }

    public void saveOwner() throws Exception {
        if (validateParty(party, true, true) && !StringUtility.isEmpty(shareId)) {
            addOwner(party, shareId);
        }
    }

    private void addOwner(ClaimParty owner, String claimShareId) {
        boolean found = false;
        for (ClaimShare claimShare : claim.getShares()) {
            if (claimShare.getId().equalsIgnoreCase(claimShareId) && (claimShare.getEntityAction() == null || !claimShare.getEntityAction().equals(EntityAction.DELETE))) {
                if (claimShare.getOwners() != null) {
                    for (ClaimParty claimParty : claimShare.getOwners()) {
                        if (claimParty.getId().equalsIgnoreCase(owner.getId()) && (claimParty.getEntityAction() == null || !claimParty.getEntityAction().equals(EntityAction.DELETE))) {
                            MappingManager.getMapper().map(owner, claimParty);
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        claimShare.getOwners().add(MappingManager.getMapper().map(owner, ClaimParty.class));
                    }
                }
                break;
            }
        }
    }

    public ClaimShare getShare() {
        if (share == null) {
            loadShare(null);
        }
        return share;
    }

    public void loadShare(String shareId) {
        share = new ClaimShare();
        if (StringUtility.isEmpty(shareId)) {
            share.setId(UUID.randomUUID().toString());
            share.setEntityAction(EntityAction.INSERT);
            share.setClaimId(claim.getId());
            share.setPercentage(100);
            share.setOwners(new ArrayList<ClaimParty>());
        } else {
            for (ClaimShare claimShare : claim.getShares()) {
                if (claimShare.getId().equalsIgnoreCase(shareId)) {
                    share.setId(claimShare.getId());
                    share.setPercentage(claimShare.getPercentage());
                    break;
                }
            }
        }
    }

    public void deleteShare(String shareId) {
        for (ClaimShare claimShare : claim.getShares()) {
            if (claimShare.getId().equalsIgnoreCase(shareId)) {
                claimShare.setEntityAction(EntityAction.DELETE);
                break;
            }
        }
    }

    public void saveShare() throws Exception {
        if (validateShare(share, true)) {
            boolean found = false;
            if (claim.getShares() == null) {
                claim.setShares(new ArrayList<ClaimShare>());
            }
            for (ClaimShare claimShare : claim.getShares()) {
                if (claimShare.getId().equalsIgnoreCase(share.getId())) {
                    claimShare.setPercentage(share.getPercentage());
                    found = true;
                    break;
                }
            }
            if (!found) {
                claim.getShares().add(share);
            }
        }
    }

    private boolean validateShare(ClaimShare claimShare, boolean throwException) throws Exception {
        if (claimShare == null) {
            return false;
        }

        boolean isValid = true;
        String errors = "";

        if (claimShare.getPercentage() <= 0) {
            if (throwException) {
                errors += "- " + msgProvider.getErrorMessage(ErrorKeys.CLAIM_SHARE_PERCENTAGE_ZERO) + "\r\n";
            } else {
                getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_SHARE_PERCENTAGE_ZERO)));
            }
            isValid = false;
        }

        if (throwException && !StringUtility.isEmpty(errors)) {
            throw new Exception(errors);
        }
        return isValid;
    }

    public boolean canEditComment(String commentId) {
        if (claim != null && claim.getComments() != null && claim.getComments().size() > 0) {
            String userName = getUserName();
            for (ClaimComment comment : claim.getComments()) {
                if (comment.getId().equalsIgnoreCase(commentId) && comment.getCommentUser().equalsIgnoreCase(userName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void deleteComment(final String commentId) {
        try {
            if (getCanEdit()) {
                for (ClaimComment comment : claim.getComments()) {
                    if (comment.getId().equalsIgnoreCase(commentId)) {
                        comment.setEntityAction(EntityAction.DELETE);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }

    public void deleteAttachment(final String attachmentId) {
        try {
            Attachment attachment = null;

            for (Attachment attach : claim.getAttachments()) {
                if (attach.getId().equalsIgnoreCase(attachmentId)) {
                    attachment = attach;

                    break;
                }
            }

            if (attachment != null) {
                if (getCanEdit()) {
                    attachment.setEntityAction(EntityAction.DELETE);
                } else if (getCanPrintCertificate()) {
                    attachment.setEntityAction(EntityAction.DELETE);
                    claimEjb.saveClaimAttachment(attachment, langBean.getLocale());
                }
            }
        } catch (Exception e) {
            LogUtility.log("Failed to delete attachment", e);
            getContext().addMessage(null, new FacesMessage(msgProvider.getErrorMessage(ErrorKeys.CLAIM_FAILED_TO_DELETE_ATTACHMENT)));
        }
    }

    public void withdrawClaim() {
        try {
            if (!StringUtility.isEmpty(id) && getCanWithdraw()) {
                runUpdate(new Runnable() {
                    @Override
                    public void run() {
                        LocalInfo.setBaseUrl(getApplicationUrl());
                        claimEjb.withdrawClaim(id);
                    }
                });
                redirectWithAction(ACTION_WITHDRAWN);
            }
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }

    public void approveReviewClaim() {
        try {
            if (!StringUtility.isEmpty(id) && getCanApproveReview()) {
                runUpdate(new Runnable() {
                    @Override
                    public void run() {
                        LocalInfo.setBaseUrl(getApplicationUrl());
                        claimEjb.approveClaimReview(id);
                    }
                });
                redirectWithAction(ACTION_REVIEW_APPROVED);
            }
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }

    public void revertClaimReview() {
        try {
            if (!StringUtility.isEmpty(id) && getCanRevertReview()) {
                runUpdate(new Runnable() {
                    @Override
                    public void run() {
                        LocalInfo.setBaseUrl(getApplicationUrl());
                        claimEjb.revertClaimReview(id);
                    }
                });
                redirectWithAction(ACTION_REVIEW_REVERTED);
            }
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }

    public void submitClaim() {
        try {
            if (!StringUtility.isEmpty(id) && getCanSubmit()) {
                runUpdate(new Runnable() {
                    @Override
                    public void run() {
                        LocalInfo.setBaseUrl(getApplicationUrl());
                        claimEjb.submitClaim(id, langBean.getLocale());
                    }
                });
                redirectWithAction(ACTION_SUBMITTED);
            }
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }

    public void approveModerationClaim() {
        try {
            if (!StringUtility.isEmpty(id) && getCanApproveModeration()) {
                runUpdate(new Runnable() {
                    @Override
                    public void run() {
                        LocalInfo.setBaseUrl(getApplicationUrl());
                        claimEjb.approveClaimModeration(id);
                    }
                });
                redirectWithAction(ACTION_MODERATION_APPROVED);
            }
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage(processException(e, langBean.getLocale()).getMessage()));
        }
    }

    private void redirectWithAction(String action) {
        try {
            getContext().getExternalContext().redirect(getRequest().getContextPath() + "/claim/ViewClaim.xhtml?id=" + claim.getId() + "&action=" + action);
        } catch (Exception e) {
            LogUtility.log("Failed to redirect", e);
        }
    }

    public String getFileSize(long size) {
        return FileUtility.formatFileSize(size);
    }
}
