package cn.bidlink.nbl.framework.auth.validator;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.jasig.cas.client.validation.Assertion;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.jasig.cas.client.validation.TicketValidationException;

import cn.bidlink.nbl.framework.auth.utils.AuthContent;

public class CustomCas20ServiceTicketValidator extends Cas20ServiceTicketValidator{

	public CustomCas20ServiceTicketValidator(String casServerUrlPrefix) {
		super(casServerUrlPrefix);
	}

	@Override
	protected void customParseResponse(String response, Assertion assertion) throws TicketValidationException {
		String domainName = (String)assertion.getPrincipal().getAttributes().get("domainName");
		String grantingTicket = (String)assertion.getPrincipal().getAttributes().get("grantingTicket");
		WebDelegatingSubject subject = (WebDelegatingSubject)SecurityUtils.getSubject();
		HttpServletRequest request = ( (HttpServletRequest)subject.getServletRequest() ) ; 
		request.setAttribute("domainName", domainName);
		request.setAttribute(AuthContent.TGT_KEY, grantingTicket);
	}
}