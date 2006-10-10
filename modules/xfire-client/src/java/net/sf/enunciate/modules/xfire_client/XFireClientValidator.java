package net.sf.enunciate.modules.xfire_client;

import net.sf.enunciate.contract.jaxws.EndpointInterface;
import net.sf.enunciate.contract.jaxws.WebMethod;
import net.sf.enunciate.contract.validation.BaseValidator;
import net.sf.enunciate.contract.validation.ValidationResult;

import javax.jws.soap.SOAPBinding;

/**
 * The validator for the xfire-client module.
 *
 * @author Ryan Heaton
 */
public class XFireClientValidator extends BaseValidator {

  @Override
  public ValidationResult validateEndpointInterface(EndpointInterface ei) {
    ValidationResult result = super.validateEndpointInterface(ei);
    for (WebMethod webMethod : ei.getWebMethods()) {
      if (webMethod.getSoapBindingStyle() == SOAPBinding.Style.RPC) {
        result.addError(webMethod.getPosition(), "XFire clients don't support RPC-style web methods.");
      }
    }
    return result;
  }
}