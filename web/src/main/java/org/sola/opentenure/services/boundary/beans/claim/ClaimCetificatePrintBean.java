package org.sola.opentenure.services.boundary.beans.claim;

import java.net.URLEncoder;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.sola.common.StringUtility;
import org.sola.opentenure.services.boundary.beans.AbstractBackingBean;
import javax.inject.Inject;
import org.sola.common.ConfigConstants;
import org.sola.cs.services.ejb.system.businesslogic.SystemCSEJBLocal;
import org.sola.opentenure.services.boundary.beans.report.ReportParam;
import org.sola.opentenure.services.boundary.beans.report.ReportServerBean;
import org.sola.opentenure.services.boundary.beans.report.ResourceDescription;
import org.sola.services.common.logging.LogUtility;

/**
 * Returns map image with claims
 */
@Named
@ViewScoped
public class ClaimCetificatePrintBean extends AbstractBackingBean {

    @EJB
    SystemCSEJBLocal systemEjb;
    
    @Inject
    ReportServerBean server;

    public void printCertificate() {
        try {
            String id = getRequestParam("id");
            if(StringUtility.isEmpty(id)){
                return;
            }
            
            ResourceDescription report = server.getReport(systemEjb.getSetting(ConfigConstants.CLAIM_CERTIFICATE_URL, ""));
            List<ReportParam> params = server.getReportParameters(report.getUri());
            String communityName = systemEjb.getSetting(ConfigConstants.COMMUNITY_NAME, "Test community");
            
            if (params != null) {
                for (ReportParam param : params) {
                    if (param.getId().equalsIgnoreCase("id")) {
                        param.setValueString(id);
                    }
                    if (param.getId().equalsIgnoreCase("communityName")) {
                        param.setValueString(URLEncoder.encode(communityName, "UTF-8"));
                    }
                    if (param.getId().equalsIgnoreCase("imageUrl")) {
                        String appUrl = getApplicationUrl();
                        // JasperServer has issues with HTTPS protocol when generating output in PDF format. 
                        // So, try to replace https to http for workaround
                        appUrl = appUrl.replace("https:", "http:").replace(":8181", ":8080").replace(":443", "");
                        param.setValueString(URLEncoder.encode(appUrl + "/claim/GetMapImage.xhtml", "UTF-8"));
                    }
                }
                server.runReport(report.getUri(), params.toArray(new ReportParam[params.size()]), "pdf");
            }
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage("Failed to run claim certificate printing"));
            LogUtility.log("Failed to run claim certificate printing", e);
        }
    }
    
    public void printForm23() {
        try {
            String id = getRequestParam("id");
            if(StringUtility.isEmpty(id)){
                return;
            }
            
            ResourceDescription report = server.getReport(systemEjb.getSetting(ConfigConstants.FORM23_URL, ""));
            List<ReportParam> params = server.getReportParameters(report.getUri());
            String communityName = systemEjb.getSetting(ConfigConstants.COMMUNITY_NAME, "Test community");
            
            if (params != null) {
                for (ReportParam param : params) {
                    if (param.getId().equalsIgnoreCase("id")) {
                        param.setValueString(id);
                    }
                    if (param.getId().equalsIgnoreCase("communityName")) {
                        param.setValueString(URLEncoder.encode(communityName, "UTF-8"));
                    }
                    if (param.getId().equalsIgnoreCase("imageUrl")) {
                        String appUrl = getApplicationUrl();
                        // JasperServer has issues with HTTPS protocol when generating output in PDF format. 
                        // So, try to replace https to http for workaround
                        appUrl = appUrl.replace("https:", "http:").replace(":8181", ":8080").replace(":443", "");
                        param.setValueString(URLEncoder.encode(appUrl + "/claim/GetMapImage.xhtml", "UTF-8"));
                    }
                }
                server.runReport(report.getUri(), params.toArray(new ReportParam[params.size()]), "pdf");
            }
        } catch (Exception e) {
            getContext().addMessage(null, new FacesMessage("Failed to run form23 printing"));
            LogUtility.log("Failed to run form23 printing", e);
        }
    }
}
