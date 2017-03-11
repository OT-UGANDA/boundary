package org.sola.opentenure.services.boundary.beans.claim;

import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Init;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;
import org.apache.sanselan.util.IOUtils;
import org.sola.common.ConfigConstants;
import org.sola.common.DateUtility;
import org.sola.common.FileUtility;
import org.sola.common.StringUtility;
import org.sola.common.mapping.MappingManager;
import org.sola.opentenure.services.boundary.beans.AbstractBackingBean;
import org.sola.opentenure.services.boundary.beans.helpers.DateBean;
import org.sola.opentenure.services.boundary.beans.helpers.ErrorKeys;
import org.sola.opentenure.services.boundary.beans.helpers.MessageProvider;
import org.sola.opentenure.services.boundary.beans.language.LanguageBean;
import org.sola.opentenure.services.boundary.beans.referencedata.ReferenceData;
import org.sola.cs.services.ejbs.claim.businesslogic.ClaimEJBLocal;
import org.sola.cs.services.ejbs.claim.entities.Attachment;
import org.sola.cs.services.ejbs.claim.entities.AttachmentBinary;
import org.sola.services.common.EntityAction;
import org.sola.services.common.LocalInfo;
import org.sola.services.common.logging.LogUtility;
import org.sola.cs.services.ejb.refdata.entities.SourceType;
import org.sola.cs.services.ejb.system.businesslogic.SystemCSEJBLocal;

/**
 * Provides method to manage claim attachments
 */
@Named
@ViewScoped
public class ClaimAttachmentBean extends AbstractBackingBean {

    @Inject
    ReferenceData refData;

    @Inject
    MessageProvider msgProvider;

    @Inject
    DateBean dateBean;

    @EJB
    ClaimEJBLocal claimEjb;

    @EJB
    SystemCSEJBLocal systemEjb;

    @Inject
    ClaimPageBean claimPageBean;

    @Inject
    LanguageBean langBean;

    private Attachment attach;
    private Part docFile;
    private boolean isNew = true;
    private List<SourceType> docTypesForIssuance = null;

    @PostConstruct
    private void init() {
        if (docTypesForIssuance == null) {
            docTypesForIssuance = claimEjb.getDocumentTypesForIssuance(langBean.getLocale());
        }
    }

    public SourceType[] getDocumentTypes() {
        SourceType[] docType;
        if (!claimPageBean.getIsTransfer() && claimPageBean.getCanIssueCertificate()) {
            // Retrurn only allowed documents
            docType = refData.getDocumentTypesForCcoIssuance(isNew, langBean.getLocale());
        } else {
            docType = refData.getDocumentTypes(isNew, langBean.getLocale(), true);
        }
        return docType;
    }

    public Attachment getAttach() {
        if (attach == null) {
            attach = new Attachment();
            attach.setId(UUID.randomUUID().toString());
        }
        return attach;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public String getDocDate() {
        return dateBean.getShortDate(getAttach().getDocumentDate());
    }

    public void setDocDate(String docDate) {
        if (!StringUtility.isEmpty(docDate)) {
            getAttach().setDocumentDate(DateUtility.convertToDate(docDate, dateBean.getDatePattern()));
        } else {
            getAttach().setDocumentDate(null);
        }
    }

    public Part getDocFile() {
        return docFile;
    }

    public void setDocFile(Part docFile) {
        this.docFile = docFile;
    }

    public void loadAttachment(String attachmentId) {
        isNew = true;
        if (StringUtility.isEmpty(attachmentId)) {
            attach = new Attachment();
            attach.setId(UUID.randomUUID().toString());
        } else {
            for (Attachment attachment : claimPageBean.getClaim().getAttachments()) {
                if (attachment.getId().equalsIgnoreCase(attachmentId) && (attachment.getEntityAction() == null || !attachment.getEntityAction().equals(EntityAction.DELETE))) {
                    MappingManager.getMapper().map(attachment, getAttach());
                    isNew = false;
                    break;
                }
            }
        }
    }

    public boolean getCanEdit(String typeCode) {
        if (claimPageBean.getCanIssueCertificate()) {
            // Check doc type which can be edited
            for (SourceType docType : docTypesForIssuance) {
                if (docType.getCode().equalsIgnoreCase(typeCode)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void saveAttachment(final boolean instantSave) throws Exception {
        if (claimPageBean != null) {
            // Make checks
            String errors = "";

            if (StringUtility.isEmpty(attach.getTypeCode())) {
                errors = "- " + msgProvider.getErrorMessage(ErrorKeys.ATTACHMENT_SELECT_DOOCUMENT_TYPE) + "\r\n";
            }

            if (isNew && docFile == null) {
                errors += "- " + msgProvider.getErrorMessage(ErrorKeys.ATTACHMENT_SELECT_FILE) + "\r\n";
            }

            if (!StringUtility.isEmpty(errors)) {
                throw new Exception(errors);
            }

            try {
                if (isNew) {
                    AttachmentBinary attachment = MappingManager.getMapper().map(attach, AttachmentBinary.class);
                    attachment.setFileName(docFile.getSubmittedFileName());
                    attachment.setFileExtension(FileUtility.getFileExtension(docFile.getSubmittedFileName()));
                    attachment.setMimeType(docFile.getContentType());
                    attachment.setSize(docFile.getSize());
                    attachment.setBody(IOUtils.getInputStreamBytes(docFile.getInputStream()));
                    attachment.setMd5(StringUtility.getMD5(attachment.getBody()));

                    final AttachmentBinary[] arg = {attachment};

                    runUpdate(new Runnable() {
                        @Override
                        public void run() {
                            LocalInfo.setBaseUrl(getApplicationUrl());
                            AttachmentBinary atth = claimEjb.saveAttachment(arg[0]);
                            claimPageBean.getClaim().getAttachments().add(atth);
                            if (instantSave) {
                                claimEjb.addClaimAttachment(claimPageBean.getClaim().getId(), attach.getId());
                            }
                        }
                    });
                } else {
                    Attachment attachmentToSave = null;
                    
                    for (Attachment attachment : claimPageBean.getClaim().getAttachments()) {
                        if (attachment.getId().equalsIgnoreCase(attach.getId()) && (attachment.getEntityAction() == null || !attachment.getEntityAction().equals(EntityAction.DELETE))) {
                            MappingManager.getMapper().map(attach, attachment);
                            attachmentToSave = attachment;
                            break;
                        }
                    }
                    
                    if (claimPageBean.getCanPrintCertificate() && attachmentToSave != null) {
                        claimEjb.saveClaimAttachment(attachmentToSave, langBean.getLocale());
                    }
                }
            } catch (Exception e) {
                LogUtility.log("Failed to save attachment", e);
                throw new Exception(msgProvider.getErrorMessage(ErrorKeys.ATTACHMENT_FAILED_TO_ATTACH));
            }
        }
    }
}
