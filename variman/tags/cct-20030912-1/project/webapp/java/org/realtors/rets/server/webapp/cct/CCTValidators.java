/*
 * Created on Aug 22, 2003
 *
 */
package org.realtors.rets.server.webapp.cct;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorUtil;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.validator.Resources;

/**
 * @author kgarner
 */
public class CCTValidators
{
    /**
     * Checks if a field is identical to a second field.  Useful for
     * cases where you are comparing passwords.  Based on work in the
     * <i>Struts in Action</i> published by Manning.
     *
     * @param bean The bean validation is being performed on.
     * @param va The ValidatorAction that is currently being performed.
     * @param field The Field object associated with the current field
     * being validated.
     * @param errors The ActionErrors object to add errors to if any
     * validation errors occur.
     * @param request Current request object.
     * @return True if valid or field blank, false otherwise.
     */
    public static boolean validateIdentical(Object bean, ValidatorAction va,
                                            Field field, ActionErrors errors,
                                            HttpServletRequest request)
    {
        String value = ValidatorUtil.getValueAsString(bean,
                                                      field.getProperty());
        String sProperty2 = field.getVarValue("secondProperty");
        String value2 = ValidatorUtil.getValueAsString(bean, sProperty2);

        if (!GenericValidator.isBlankOrNull(value))
        {
            if (!value.equals(value2))
            {
                errors.add(field.getKey(),
                           Resources.getActionError(request, va, field));
                return false;
            }
        }
        return true;
    }

}
