package org.sola.opentenure.services.boundary.beans.referencedata;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import org.sola.common.ConfigConstants;
import org.sola.common.StringUtility;
import org.sola.cs.services.ejbs.claim.entities.ClaimStatus;
import org.sola.cs.services.ejbs.claim.entities.FieldConstraintType;
import org.sola.cs.services.ejbs.claim.entities.LandUse;
import org.sola.cs.services.ejb.refdata.entities.LandUseType;
import org.sola.cs.services.boundary.transferobjects.system.MapExtentTO;
import org.sola.services.common.repository.entities.AbstractCodeEntity;
import org.sola.cs.services.ejb.cache.businesslogic.CacheCSEJBLocal;
import org.sola.cs.services.ejb.search.businesslogic.SearchCSEJBLocal;
import org.sola.cs.services.ejb.system.businesslogic.SystemCSEJBLocal;
import org.sola.cs.services.ejb.refdata.businesslogic.RefDataCSEJBLocal;
import org.sola.cs.services.ejb.refdata.entities.FieldType;
import org.sola.cs.services.ejb.refdata.entities.GenderType;
import org.sola.cs.services.ejb.refdata.entities.IdType;
import org.sola.cs.services.ejb.refdata.entities.Language;
import org.sola.cs.services.ejb.refdata.entities.RejectionReason;
import org.sola.cs.services.ejb.refdata.entities.RrrType;
import org.sola.cs.services.ejb.refdata.entities.SourceType;
import org.sola.cs.services.ejbs.claim.businesslogic.ClaimEJBLocal;

/**
 * Holds methods to retrieve reference data. ALl data are cached when first time
 * received from DB.
 */
@Named
@ApplicationScoped
public class ReferenceData {
    
    @EJB
    SearchCSEJBLocal searchEjb;

    @EJB
    SystemCSEJBLocal systemEjb;
    
    @EJB
    RefDataCSEJBLocal refDataEjb;
    
    @EJB
    ClaimEJBLocal claimEjb;
    
    @EJB
    CacheCSEJBLocal cacheEjb;

    private final String MAP_EXTENT = "MAP_EXTENT";
    private final String COMMUNITY_AREA = "COMMUNITY_AREA";
    
    public ReferenceData() {
        super();
    }
        
    /**
     * Returns list of {@link Language}
     *
     * @param langCode Language code
     * @return
     */
    public List<Language> getLanguages(String langCode) {
        return refDataEjb.getLanguages(langCode);
    }
    
    /**
     * Returns list of {@link FieldType}
     *
     * @param langCode Language code
     * @param onlyActive Indicates whether to return only active records or all.
     * @return
     */
    public List<FieldType> getFieldTypes(String langCode, boolean onlyActive) {
        return getTypes(refDataEjb.getCodeEntityList(FieldType.class, langCode),onlyActive);
    }

    /**
     * Returns array of {@link FieldType}
     *
     * @param addDummy Indicates whether to create dummy record or not
     * @param langCode Language code
     * @param onlyActive Indicates whether to return only active records or all.
     * @return
     */
    public FieldType[] getFieldTypes(boolean addDummy, String langCode, boolean onlyActive) {
        ArrayList<FieldType> result = (ArrayList<FieldType>) getFieldTypes(langCode, onlyActive);
        if (addDummy) {
            result = (ArrayList<FieldType>) result.clone();
            result.add(0, createDummy(new FieldType()));
        }
        return result.toArray(new FieldType[result.size()]);
    }
    
    /**
     * Returns list of {@link FieldConstraintType}
     *
     * @param langCode Language code
     * @param onlyActive Indicates whether to return only active records or all.
     * @return
     */
    public List<FieldConstraintType> getFieldConstraintTypes(String langCode, boolean onlyActive) {
        return getTypes(refDataEjb.getCodeEntityList(FieldConstraintType.class, langCode), onlyActive);
    }

    /**
     * Returns array of {@link FieldConstraintType}
     *
     * @param addDummy Indicates whether to create dummy record or not
     * @param langCode Language code
     * @param onlyActive Indicates whether to return only active records or all.
     * @return
     */
    public FieldConstraintType[] getFieldConstraintTypes(boolean addDummy, String langCode, boolean onlyActive) {
        ArrayList<FieldConstraintType> result = (ArrayList<FieldConstraintType>) getFieldConstraintTypes(langCode, onlyActive);
        if (addDummy) {
            result = (ArrayList<FieldConstraintType>) result.clone();
            result.add(0, createDummy(new FieldConstraintType()));
        }
        return result.toArray(new FieldConstraintType[result.size()]);
    }
    
    /**
     * Returns list of {@link LandUseType}
     *
     * @param langCode Language code
     * @param onlyActive Indicates whether to return only active records or all.
     * @return
     */
    public List<LandUseType> getLandUses(String langCode, boolean onlyActive) {
        return getTypes(refDataEjb.getCodeEntityList(LandUseType.class, langCode), onlyActive);
    }

    /**
     * Returns array of {@link LandUse}
     *
     * @param addDummy Indicates whether to create dummy record or not
     * @param langCode Language code
     * @param onlyActive Indicates whether to return only active records or all.
     * @return
     */
    public LandUseType[] getLandUses(boolean addDummy, String langCode, boolean onlyActive) {
        ArrayList<LandUseType> result = (ArrayList<LandUseType>) getLandUses(langCode, onlyActive);
        if (addDummy) {
            result = (ArrayList<LandUseType>) result.clone();
            result.add(0, createDummy(new LandUseType()));
        }
        return result.toArray(new LandUseType[result.size()]);
    }

    /**
     * Returns list of {@link RejectionReason}
     *
     * @param langCode Language code
     * @param onlyActive Indicates whether to return only active records or all.
     * @return
     */
    public List<RejectionReason> getRejectionReasons(String langCode, boolean onlyActive) {
        return getTypes(refDataEjb.getCodeEntityList(RejectionReason.class, langCode), onlyActive);
    }

    /**
     * Returns array of {@link RejectionReason}
     *
     * @param addDummy Indicates whether to create dummy record or not
     * @param langCode Language code
     * @param onlyActive Indicates whether to return only active records or all.
     * @return
     */
    public RejectionReason[] getRejectionReasons(boolean addDummy, String langCode, boolean onlyActive) {
        ArrayList<RejectionReason> result = (ArrayList<RejectionReason>) getRejectionReasons(langCode, onlyActive);
        if (addDummy) {
            result = (ArrayList<RejectionReason>) result.clone();
            result.add(0, createDummy(new RejectionReason()));
        }
        return result.toArray(new RejectionReason[result.size()]);
    }

    /**
     * Returns list of {@link RrrType}
     *
     * @param langCode Language code
     * @param onlyActive Indicates whether to return only active records or all.
     * @return
     */
    public List<RrrType> getRightTypes(String langCode, boolean onlyActive) {
        return getTypes(refDataEjb.getCodeEntityList(RrrType.class, langCode), onlyActive);
    }

    /**
     * Returns array of {@link RrrType}
     *
     * @param addDummy Indicates whether to create dummy record or not
     * @param langCode Language code
     * @param onlyActive Indicates whether to return only active records or all.
     * @return
     */
    public RrrType[] getRightTypes(boolean addDummy, String langCode, boolean onlyActive) {
        ArrayList<RrrType> result = (ArrayList<RrrType>) getRightTypes(langCode, onlyActive);
        if (addDummy) {
            result = (ArrayList<RrrType>) result.clone();
            result.add(0, createDummy(new RrrType()));
        }
        return result.toArray(new RrrType[result.size()]);
    }

    /**
     * Returns list of {@link ClaimStatus}
     *
     * @param langCode Language code
     * @return
     */
    public List<ClaimStatus> getClaimStatuses(String langCode) {
        return refDataEjb.getCodeEntityList(ClaimStatus.class, langCode);
    }

    /**
     * Returns list of {@link SourceType}
     *
     * @param langCode Language code
     * @param onlyActive Indicates whether to return only active records or all.
     * @return
     */
    public List<SourceType> getDocumentTypes(String langCode, boolean onlyActive) {
        return getTypes(refDataEjb.getCodeEntityList(SourceType.class, langCode), onlyActive);
    }
    
    /**
     * Returns list of {@link SourceType} for CCO issuance
     *
     * @param langCode Language code
     * @return
     */
    public List<SourceType> getDocumentTypesForCcoIssuance(String langCode) {
        return claimEjb.getDocumentTypesForIssuance(langCode);
    }

    /**
     * Returns list of {@link IdType}
     *
     * @param langCode Language code
     * @param onlyActive Indicates whether to return only active records or all.
     * @return
     */
    public List<IdType> getIdTypes(String langCode, boolean onlyActive) {
        return getTypes(refDataEjb.getCodeEntityList(IdType.class, langCode), onlyActive);
    }

    /**
     * Returns array of {@link IdType}
     *
     * @param addDummy Indicates whether to create dummy record or not
     * @param langCode Language code
     * @param onlyActive Indicates whether to return only active records or all.
     * @return
     */
    public IdType[] getIdTypes(boolean addDummy, String langCode, boolean onlyActive) {
        ArrayList<IdType> result = (ArrayList<IdType>) getIdTypes(langCode, onlyActive);
        if (addDummy) {
            result = (ArrayList<IdType>) result.clone();
            result.add(0, createDummy(new IdType()));
        }
        return result.toArray(new IdType[result.size()]);
    }

    /**
     * Returns list of {@link GenderType}
     *
     * @param langCode Language code
     * @param onlyActive Indicates whether to return only active records or all.
     * @return
     */
    public List<GenderType> getGenderTypes(String langCode, boolean onlyActive) {
        return getTypes(refDataEjb.getCodeEntityList(GenderType.class, langCode), onlyActive);
    }

    /**
     * Returns array of {@link GenderType}
     *
     * @param addDummy Indicates whether to create dummy record or not
     * @param langCode Language code
     * @param onlyActive Indicates whether to return only active records or all.
     * @return
     */
    public GenderType[] getGenderTypes(boolean addDummy, String langCode, boolean onlyActive) {
        ArrayList<GenderType> result = (ArrayList<GenderType>) getGenderTypes(langCode, onlyActive);
        if (addDummy) {
            result = (ArrayList<GenderType>) result.clone();
            result.add(0, createDummy(new GenderType()));
        }
        return result.toArray(new GenderType[result.size()]);
    }

    /**
     * Returns map extent
     *
     * @return
     */
    public MapExtentTO getMapExtent() {
        if (cacheEjb.containsKey(MAP_EXTENT)) {
            return (MapExtentTO) cacheEjb.get(MAP_EXTENT);
        }

        HashMap<String, String> mapSettings = searchEjb.getMapSettingList();
        MapExtentTO result = null;

        if (mapSettings != null) {
            result = new MapExtentTO();
            result.setMinX(mapSettings.get("map-west"));
            result.setMaxY(mapSettings.get("map-north"));
            result.setMaxX(mapSettings.get("map-east"));
            result.setMinY(mapSettings.get("map-south"));
        }

        if (result != null) {
            cacheEjb.put(MAP_EXTENT, result);
        }
        return result;
    }

    /**
     * Returns list of {@link SourceType}
     *
     * @param addDummy If true, empty item will be inserted on the top
     * @return
     */
    public SourceType[] getDocumentTypes(boolean addDummy, String langCode, boolean onlyActive) {
        ArrayList<SourceType> result = (ArrayList<SourceType>) getDocumentTypes(langCode, onlyActive);
        if (addDummy) {
            result = (ArrayList<SourceType>) result.clone();
            result.add(0, createDummy(new SourceType()));
        }
        return result.toArray(new SourceType[result.size()]);
    }
    
    /**
     * Returns list of {@link SourceType} for certificate issuance
     *
     * @param addDummy If true, empty item will be inserted on the top
     * @return
     */
    public SourceType[] getDocumentTypesForCcoIssuance(boolean addDummy, String langCode) {
        ArrayList<SourceType> result = (ArrayList<SourceType>) getDocumentTypesForCcoIssuance(langCode);
        if (addDummy) {
            result = (ArrayList<SourceType>) result.clone();
            result.add(0, createDummy(new SourceType()));
        }
        return result.toArray(new SourceType[result.size()]);
    }

    /**
     * Returns list of {@link ClaimStatus}
     *
     * @param addDummy If true, empty item will be inserted on the top
     * @return
     */
    public ClaimStatus[] getClaimStatuses(boolean addDummy, String langCode) {
        ArrayList<ClaimStatus> result = (ArrayList<ClaimStatus>) getClaimStatuses(langCode);
        if (addDummy) {
            result = (ArrayList<ClaimStatus>) result.clone();
            result.add(0, createDummy(new ClaimStatus()));
        }
        return result.toArray(new ClaimStatus[result.size()]);
    }

    /**
     * Returns community area, defining where claims can be submitted. Returns
     * in WKT format.
     * @return 
     */
    public String getCommunityArea() {
        if (cacheEjb.containsKey(COMMUNITY_AREA)) {
            return cacheEjb.get(COMMUNITY_AREA).toString();
        }

        String result = systemEjb.getSetting(ConfigConstants.OT_COMMUNITY_AREA, "");

        if (result != null) {
            cacheEjb.put(COMMUNITY_AREA, result);
        }
        return result;
    }

    /**
     * Searches provided list of beans by bean code and returns found object.
     *
     * @param <T> Type of bean
     * @param list List of beans
     * @param code Bean code value
     * @return Returns bean or null if it wasn't found.
     */
    public <T extends AbstractCodeEntity> T getBeanByCode(List<T> list, String code) {
        if (list == null || list.size() < 1 || StringUtility.isEmpty(code)) {
            return null;
        }

        T result = null;
        for (Iterator<T> it = list.iterator(); it.hasNext();) {
            AbstractCodeEntity bean = it.next();
            if (bean.getCode().equals(code)) {
                result = (T) bean;
                break;
            }
        }
        return result;
    }

    /**
     * Generic method to filter items in the provided list by status
     */
    private <T extends AbstractCodeEntity> List<T> getTypes(List<T> result, boolean onlyActive) {
        if (result != null) {
            if (onlyActive) {
                List<T> active = new ArrayList<>();
                for (T item : result) {
                    if (item.getStatus().equalsIgnoreCase("c")) {
                        active.add(item);
                    }
                }
                result = active;
            }
        }
        return result;
    }

    /**
     * Searches provided list of beans by bean code and returns display name
     * value.
     *
     * @param <T> Type of bean
     * @param list List of beans
     * @param code Bean code value
     * @return Returns name or empty value if it wasn't found.
     */
    public <T extends AbstractCodeEntity> String getBeanDisplayValue(List<T> list, String code) {
        if (list == null || list.size() < 1 || StringUtility.isEmpty(code)) {
            return "";
        }

        T result = getBeanByCode(list, code);
        if (result == null) {
            return "";
        }
        return result.getDisplayValue();
    }

    private String makeKey(String key, String lang) {
        if(StringUtility.isEmpty(lang))
            return key + "_all";
        else
            return key + "_" + lang;
    }

    private <T extends AbstractCodeEntity> T createDummy(T entity) {
        entity.setCode("");
        entity.setDisplayValue(" ");
        return entity;
    }
}
