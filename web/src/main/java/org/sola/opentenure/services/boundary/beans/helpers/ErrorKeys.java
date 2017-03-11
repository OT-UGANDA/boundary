package org.sola.opentenure.services.boundary.beans.helpers;

/**
 * Holds list of error keys, used to extract messages from errors bundle
 */
public class ErrorKeys {

// General erros
    /**
     * Form Submission was not successful. Please review and correct listed
     * errors:
     */
    public static final String GENERAL_ERROR_LIST_HEADER = "GENERAL_ERROR_LIST_HEADER";

    /**
     * Unexpected errors have occured while executing requested action :
     */
    public static final String GENERAL_UNEXPECTED_ERROR = "GENERAL_UNEXPECTED_ERROR";

    /** Failed to redirect */
    public static final String GENERAL_REDIRECT_FAILED = "GENERAL_REDIRECT_FAILED";
    
// Login erros
    /**
     * Provide user name
     */
    public static final String LOGIN_USERNAME_REQUIRED = "LOGIN_USERNAME_REQUIRED";

    /**
     * Provide password
     */
    public static final String LOGIN_PASSWORD_REQUIRED = "LOGIN_PASSWORD_REQUIRED";

    /**
     * Login failed
     */
    public static final String LOGIN_FAILED = "LOGIN_FAILED";

    /**
     * Logout failed
     */
    public static final String LOGIN_LOGOUT_FAILED = "LOGIN_LOGOUT_FAILED";

    /**
     * Your account is not active. If you just registered, you need to activate
     * it first.
     */
    public static final String LOGIN_ACCOUNT_BLOCKED = "LOGIN_ACCOUNT_BLOCKED";

// Registration errors
    /**
     * Entered captcha value doesn't match with image.
     */
    public static final String USER_REGISTRATION_WRONG_CAPTCHA = "USER_REGISTRATION_WRONG_CAPTCHA";

    /**
     * Provided Email address has incorrect format
     */
    public static final String USER_REGISTRATION_BAD_EMAIL = "USER_REGISTRATION_BAD_EMAIL";

    /**
     * Provided mobile number has incorrect format
     */
    public static final String USER_REGISTRATION_BAD_MOBILE = "USER_REGISTRATION_BAD_MOBILE";

    /**
     * Password and password confirmation doesn't match
     */
    public static final String USER_REGISTRATION_PASSWORDS_MISMATCH = "USER_REGISTRATION_PASSWORDS_MISMATCH";

    /**
     * Provided user name already exists
     */
    public static final String USER_REGISTRATION_USER_EXISTS = "USER_REGISTRATION_USER_EXISTS";

    /**
     * Provided Email address already exists
     */
    public static final String USER_REGISTRATION_EMAIL_EXISTS = "USER_REGISTRATION_EMAIL_EXISTS";

    /**
     * User name length must be between 3-20 characters
     */
    public static final String USER_REGISTRATION_USERNAME_LENGTH = "USER_REGISTRATION_USERNAME_LENGTH";

    /**
     * Provide password confirmation
     */
    public static final String USER_REGISTRATION_PASSWORD_CONFIRMATION_REQUIRED = "USER_REGISTRATION_PASSWORD_CONFIRMATION_REQUIRED";

    /**
     * Provide captcha value
     */
    public static final String USER_REGISTRATION_CAPTCHA_REQUIRED = "USER_REGISTRATION_CAPTCHA_REQUIRED";

    /**
     * Provide first name
     */
    public static final String USER_REGISTRATION_FIRSTNAME_REQUIRED = "USER_REGISTRATION_FIRSTNAME_REQUIRED";

    /**
     * Provide last name
     */
    public static final String USER_REGISTRATION_LASTNAME_REQUIRED = "USER_REGISTRATION_LASTNAME_REQUIRED";

    /**
     * Provide email
     */
    public static final String USER_REGISTRATION_EMAIL_REQUIRED = "USER_REGISTRATION_EMAIL_REQUIRED";

    /**
     * Provide mobile phone number
     */
    public static final String USER_REGISTRATION_MOBILE_REQUIRED = "USER_REGISTRATION_MOBILE_REQUIRED";

// Activation errors
    /**
     * Activation failed. Please, check activation code and/or user name.
     */
    public static final String USER_ACTIVATION_ACTIVATION_FAILED = "USER_ACTIVATION_ACTIVATION_FAILED";

    /**
     * Provide activation code
     */
    public static final String USER_ACTIVATION_CODE_REQUIRED = "USER_ACTIVATION_CODE_REQUIRED";

    /**
     * Provided activation code doesn't match provided user name
     */
    public static final String USER_ACTIVATION_BAD_CODE = "USER_ACTIVATION_BAD_CODE";

// User profile errors
    /**
     * The current password is wrong.
     */
    public static final String PROFILE_BAD_OLD_PASSWORD = "PROFILE_BAD_OLD_PASSWORD";

    /**
     * Failed to change password.
     */
    public static final String PROFILE_FAILED_CHANGE_PASSWORD = "PROFILE_FAILED_CHANGE_PASSWORD";

    /**
     * Provide current password
     */
    public static final String PROFILE_CURRENT_PASSWORD_REQUIRED = "PROFILE_CURRENT_PASSWORD_REQUIRED";

// Claim errors
    /**
     * Failed to delete claim
     */
    public static final String CLAIM_FAILED_TO_DELETE = "CLAIM_FAILED_TO_DELETE";
    /**
     * Failed to assign claim
     */
    public static final String CLAIM_FAILED_TO_ASSIGN = "CLAIM_FAILED_TO_ASSIGN";
    /**
     * Failed to unassign claim
     */
    public static final String CLAIM_FAILED_TO_UNASSIGN = "CLAIM_FAILED_TO_UNASSIGN";
    /**
     * Failed to reject claim
     */
    public static final String CLAIM_FAILED_TO_REJECT = "CLAIM_FAILED_TO_REJECT";
    /**
     * Failed to approve claim review
     */
    public static final String CLAIM_FAILED_TO_APPROVE_REVIEW = "CLAIM_FAILED_TO_APPROVE_REVIEW";
    /**
     * Failed to approve claim moderation
     */
    public static final String CLAIM_FAILED_TO_APPROVE_MODERATION = "CLAIM_FAILED_TO_APPROVE_MODERATION";
    /**
     * Failed to withdraw claim
     */
    public static final String CLAIM_FAILED_TO_WITHDRAW = "CLAIM_FAILED_TO_WITHDRAW";
    /**
     * Failed to save claim comment
     */
    public static final String CLAIM_FAILED_TO_SAVE_COMMENT = "CLAIM_FAILED_TO_SAVE_COMMENT";
    /**
     * Failed to delete comment
     */
    public static final String CLAIM_FAILED_TO_DELETE_COMMENT = "CLAIM_FAILED_TO_DELETE_COMMENT";
    /**
     * Failed to save claim
     */
    public static final String CLAIM_FAILED_TO_SAVE = "CLAIM_FAILED_TO_SAVE";
    /**
     * Failed to delete attachment
     */
    public static final String CLAIM_FAILED_TO_DELETE_ATTACHMENT = "CLAIM_FAILED_TO_DELETE_ATTACHMENT";
    /**
     * Claim can't be edited. Make sure you have sufficient rights and claim
     * status is appropriate
     */
    public static final String CLAIM_EDIT_NOT_ALLOWED = "CLAIM_EDIT_NOT_ALLOWED";
    /**
     * Claim can't be transferred. Make sure you have sufficient rights and claim
     * status is appropriate
     */
    public static final String CLAIM_TRANSFER_NOT_ALLOWED = "CLAIM_TRANSFER_NOT_ALLOWED";
    /**
     * Claim type is required
     */
    public static final String CLAIM_TYPE_REQUIRED = "CLAIM_TYPE_REQUIRED";
    /**
     * Challenge expiry date is required
     */
    public static final String CLAIM_CHALLENGE_EXPIRATION_DATE_REQUIRED = "CLAIM_CHALLENGE_EXPIRATION_DATE_REQUIRED";
    /**
     * Claimant is required
     */
    public static final String CLAIM_CLAIMANT_REQUIRED = "CLAIM_CLAIMANT_REQUIRED";
    /**
     * Land use is required
     */
    public static final String CLAIM_LAND_USE_REQUIRED = "CLAIM_LAND_USE_REQUIRED";
    /**
     * Geometry is required
     */
    public static final String CLAIM_MAPPED_GEOMETRY_REQUIRED = "CLAIM_MAPPED_GEOMETRY_REQUIRED";
    /**
     * Challenged claim is not found
     */
    public static final String CLAIM_CHALLENGED_CLAIM_NOT_FOUND = "CLAIM_CHALLENGED_CLAIM_NOT_FOUND";
    /**
     * Challenged claim can't be challenged. Check claim status.
     */
    public static final String CLAIM_CHALLENGED_CLAIM_CANT_CHALLENGED = "CLAIM_CHALLENGED_CLAIM_CANT_CHALLENGED";
    /**
     * Owner name is required
     */
    public static final String CLAIM_OWNER_NAME_REQUIRED = "CLAIM_OWNER_NAME_REQUIRED";
    /**
     * Claimant name is required
     */
    public static final String CLAIM_CLAIMANT_NAME_REQUIRED = "CLAIM_CLAIMANT_NAME_REQUIRED";
    /**
     * Owner type is required
     */
    public static final String CLAIM_OWNER_TYPE_REQUIRED = "CLAIM_OWNER_TYPE_REQUIRED";
    /**
     * Claimant type is required
     */
    public static final String CLAIM_CLAIMANT_TYPE_REQUIRED = "CLAIM_CLAIMANT_TYPE_REQUIRED";
    /**
     * Share denominator is required
     */
    public static final String CLAIM_SHARE_DENOMINATOR_REQUIRED = "CLAIM_SHARE_DENOMINATOR_REQUIRED";
    /**
     * Share numerator is required
     */
    public static final String CLAIM_SHARE_NUMERATOR_REQUIRED = "CLAIM_SHARE_NUMERATOR_REQUIRED";
    /**
     * Share percentage is required
     */
    public static final String CLAIM_SHARE_PERCENTAGE_REQUIRED = "CLAIM_SHARE_PERCENTAGE_REQUIRED";
    /**
     * Share percentage must be grater than 0
     */
    public static final String CLAIM_SHARE_PERCENTAGE_ZERO = "CLAIM_SHARE_PERCENTAGE_ZERO";
    /**
     * At least one share is required
     */
    public static final String CLAIM_SHARES_REQUIRED = "CLAIM_SHARES_REQUIRED";
    /**
     * All shares must have at least 1 owner
     */
    public static final String CLAIM_SHARE_OWNER_REQUIRED = "CLAIM_SHARE_OWNER_REQUIRED";
    /**
     * Total shares must result into 100%
     */
    public static final String CLAIM_SHARE_TOTAL_SHARE_WRONG = "CLAIM_SHARE_TOTAL_SHARE_WRONG";
    /**
     * Numerator can't be 0 or less than 0
     */
    public static final String CLAIM_SHARE_ZERO_NUMERATOR = "CLAIM_SHARE_ZERO_NUMERATOR";
    /**
     * Denominator can't be 0 or less than 0
     */
    public static final String CLAIM_SHARE_ZERO_DENOMINATOR = "CLAIM_SHARE_ZERO_DENOMINATOR";
    /**
     * Rejection reason is required
     */
    public static final String CLAIM_REJECTION_REASON_REQUIRED = "CLAIM_REJECTION_REASON_REQUIRED";
    /**
     * Failed to submit claim
     */
    public static final String CLAIM_FAILED_TO_SUBMIT = "CLAIM_FAILED_TO_SUBMIT";
    /**
     * Claim not found
     */
    public static final String CLAIM_NOT_FOUND = "CLAIM_NOT_FOUND";

    // Password restore
    /**
     * Password restore code not found
     */
    public static final String PASSWORD_RESTORE_RESTORE_CODE_NOT_FOUND = "PASSWORD_RESTORE_RESTORE_CODE_NOT_FOUND";

    /**
     * Your account is locked and password can not be restored
     */
    public static final String PASSWORD_RESTORE_ACCOUNT_LOCKED = "PASSWORD_RESTORE_ACCOUNT_LOCKED";

    /**
     * User account with given email was not found.
     */
    public static final String PASSWORD_RESTORE_EMAIL_NOT_FOUND = "PASSWORD_RESTORE_EMAIL_NOT_FOUND";

// Attachment errors
    /**
     * Failed to attach document
     */
    public static final String ATTACHMENT_FAILED_TO_ATTACH = "ATTACHMENT_FAILED_TO_ATTACH";
    /**
     * Select document type
     */
    public static final String ATTACHMENT_SELECT_DOOCUMENT_TYPE = "ATTACHMENT_SELECT_DOOCUMENT_TYPE";
    /**
     * Select file to attach
     */
    public static final String ATTACHMENT_SELECT_FILE = "ATTACHMENT_SELECT_FILE";
    /**
     * Wrong document date
     */
    public static final String ATTACHMENT_BAD_DOC_DATE = "ATTACHMENT_BAD_DOC_DATE";

    // Claim upload
    /**
     * Claim zip file is required.
     */
    public static final String CLAIM_UPLOAD_FILE_REQUIERD = "CLAIM_UPLOAD_FILE_REQUIERD";
    /**
     * Incorrect claim folder structure.
     */
    public static final String CLAIM_UPLOAD_BAD_FOLDER_STRUCTURE = "CLAIM_UPLOAD_BAD_FOLDER_STRUCTURE";
    /**
     * Json file not found.
     */
    public static final String CLAIM_UPLOAD_JSON_NOT_FOUND = "CLAIM_UPLOAD_JSON_NOT_FOUND";
    /**
     * Attachment file "%s" not found.
     */
    public static final String CLAIM_UPLOAD_ATTACHMENT_NOT_FOUND = "CLAIM_UPLOAD_ATTACHMENT_NOT_FOUND";
    /**
     * Failed to extract files from archive. Make sure archive is not broken. If
     * file is protected with password, make sure you have provided correct
     * password.
     */
    public static final String CLAIM_UPLOAD_ZIP_EXTRACTION_FAILED = "CLAIM_UPLOAD_ZIP_EXTRACTION_FAILED";

    // Map control
    /**
     * Additional location description required
     */
    public static final String MAP_CONTROL_CLAIM_ADDITIONAL_LOCATION_DESCRIPTION_REQUIRED = "MAP_CONTROL_CLAIM_ADDITIONAL_LOCATION_DESCRIPTION_REQUIRED";

    // Dynamic forms
    /**
     * Form template cannot be deleted because it has related claim records.
     */
    public static final String FORMS_PAGE_FORM_HAS_PAYLOAD = "FORMS_PAGE_FORM_HAS_PAYLOAD";
    /**
     * - Fill in name
     */
    public static final String FORMS_PAGE_FILL_NAME = "FORMS_PAGE_FILL_NAME";
    /**
     * - Fill in display name
     */
    public static final String FORMS_PAGE_FILL_DISPLAY_NAME = "FORMS_PAGE_FILL_DISPLAY_NAME";
    /**
     * - Fill in element name
     */
    public static final String FORMS_PAGE_FILL_ELEMENT_NAME = "FORMS_PAGE_FILL_ELEMENT_NAME";
    /**
     * - Fill in element display name
     */
    public static final String FORMS_PAGE_FILL_ELEMENT_DISPLAY_NAME = "FORMS_PAGE_FILL_ELEMENT_DISPLAY_NAME";
    /**
     * - Fill in error message
     */
    public static final String FORMS_PAGE_FILL_ERROR_MESSAGE = "FORMS_PAGE_FILL_ERROR_MESSAGE";
    /**
     * - Minimum occurrence should not be grater than maximum occurrence
     */
    public static final String FORMS_PAGE_MIN_OCCUR_GRATER_MAX_OCCUR = "FORMS_PAGE_MIN_OCCUR_GRATER_MAX_OCCUR";
    /**
     * - Fill in hint
     */
    public static final String FORMS_PAGE_FILL_HINT = "FORMS_PAGE_FILL_HINT";
    /**
     * - Select field type
     */
    public static final String FORMS_PAGE_SELECT_FIELD_TYPE = "FORMS_PAGE_SELECT_FIELD_TYPE";
    /**
     * - Select field constraint type
     */
    public static final String FORMS_PAGE_SELECT_FIELD_CONSTRAINT_TYPE = "FORMS_PAGE_SELECT_FIELD_CONSTRAINT_TYPE";
    /**
     * Fill in form name and display name
     */
    public static final String FORMS_PAGE_FILL_FORM_NAME_AND_DISPLAY_NAME = "FORMS_PAGE_FILL_FORM_NAME_AND_DISPLAY_NAME";
    /**
     * Add at least 1 section
     */
    public static final String FORMS_PAGE_ADD_1_SECTION = "FORMS_PAGE_ADD_1_SECTION";
    /**
     * Add at least 1 field into section %s
     */
    public static final String FORMS_PAGE_ADD_1_FIELD = "FORMS_PAGE_ADD_1_FIELD";
    
    // Reprots
    /**
     * Failed to authenticate. HTTP error code: %s
     */
    public static final String REPORTS_FAILED_AUTHENTICATE = "REPORTS_FAILED_AUTHENTICATE";
    /**
     * Failed to get list of reports. HTTP error code: %s
     */
    public static final String REPORTS_FAILED_TO_GET_REPORTS = "REPORTS_FAILED_TO_GET_REPORTS";
    /**
     * Failed to get resource. HTTP error code: %s
     */
    public static final String REPORTS_FAILED_TO_GET_RESOURCE = "REPORTS_FAILED_TO_GET_RESOURCE";
    /**
     * Failed to get list of report parameters. HTTP error code: %s
     */
    public static final String REPORTS_FAILED_TO_GET_PARAMS = "REPORTS_FAILED_TO_GET_PARAMS";
    /**
     * Failed to get report. HTTP error code: %s
     */
    public static final String REPORTS_FAILED_TO_GET_REPORT = "REPORTS_FAILED_TO_GET_REPORT";
    /**
     * - Fill in "%s"
     */
    public static final String REPORTS_FILL_IN_PARAM = "REPORTS_FILL_IN_PARAM";
    /**
     * Parameter "%s" is not a valid date
     */
    public static final String REPORTS_PARAM_IS_NOT_DATE = "REPORTS_PARAM_IS_NOT_DATE";
    /**
     * Parameter "%s" is not a valid number
     */
    public static final String REPORTS_PARAM_IS_NOT_NUMBER = "REPORTS_PARAM_IS_NOT_NUMBER";
    
    /**
     * Requested report was not found.
     */
    public static final String REPORT_NOT_FOUND = "REPORT_NOT_FOUND";
    
    /**
     * You don't have rights to access this page
     */
    public static final String GENERAL_INSUFFICIENT_RIGHTS = "GENERAL_INSUFFICIENT_RIGHTS";
}
