package com.kdh.klub;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.csvreader.CsvReader;
import com.liferay.portal.NoSuchOrganizationException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Organization;
import com.liferay.portal.service.OrganizationLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.ServiceContextFactory;
import com.liferay.portal.theme.ThemeDisplay;
import com.liferay.portal.util.PortalUtil;
import com.liferay.util.bridges.mvc.MVCPortlet;

/**
 * Portlet implementation class Import
 */
public class Import extends MVCPortlet {
 
	private static final char DELIMITER = ";".charAt(0);
	private final static Charset UNICODE_CHARSET = Charset.forName("UTF-8");
	
	private final static String STRANA = "strana";
	private final static String KRAJ = "kraj";
	private final static String OKRES = "okres";
	private final static String OBEC = "obec";
	private final static String KLUB = "klub";
	
	private final static String OKRES_PREFIX = "OC ";
	private final static String OBEC_PREFIX = "";
	private final static String KLUB_PREFIX = "KDK ";
	
	public void addKlubs(ActionRequest actionRequest, ActionResponse actionResponse) 
			throws IOException, PortalException, SystemException{
		
		StringBuilder sb = new StringBuilder();
	
		
		UploadPortletRequest uploadPortletRequest =
				PortalUtil.getUploadPortletRequest(actionRequest);
		
		ThemeDisplay themeDisplay = (ThemeDisplay)actionRequest.getAttribute(
				WebKeys.THEME_DISPLAY);
		
		long companyId = themeDisplay.getCompanyId();
		
		//Country country = CountryServiceUtil.getCountryByName("Slovakia");
		
		long countryId = 192;
		long regionId = 0;
		int statusId = 12017;
		
		String kdhOrgName = "KDH";
		long kdhOrgId = 0;
		
		Organization kdh;
		Organization krajOrg;
		Organization okresOrg;
		Organization obecOrg;
		Organization klubOrg;
		
		long userId = themeDisplay.getUserId();
		long parentOrganizationId = 0;
		
		String comments = null;
		String type = STRANA;
		boolean site = true;
		String name = kdhOrgName;
		
		
		
		ServiceContext serviceContext = ServiceContextFactory.getInstance(
				Group.class.getName(), actionRequest);
		
		//TODO create organization type
		try{
			kdh = OrganizationLocalServiceUtil.getOrganization(companyId, kdhOrgName);
			
			System.out.println(kdhOrgName + " existuje");
			
			kdhOrgId = kdh.getOrganizationId();
		}
		catch(NoSuchOrganizationException e ){
			
			kdh = OrganizationLocalServiceUtil.addOrganization(userId, parentOrganizationId, name, type, 
					regionId, countryId, statusId, comments, site, serviceContext);
			
			System.out.println(kdhOrgName + " neexistuje, VYTVARAM");
			
			
			kdhOrgId = kdh.getOrganizationId();
		}
				
		InputStream fileImportStream = null;
		
		fileImportStream =
				uploadPortletRequest.getFileAsStream("csvFile");
		
		System.out.println(" subor nacitany");
		
		try{
		
			CsvReader kluby = new CsvReader(fileImportStream, DELIMITER, UNICODE_CHARSET);
			
			kluby.readHeaders();
			
			
	
			while(kluby.readRecord()){
				
				String krajName = kluby.get("kraj");
				String okresName = OKRES_PREFIX + kluby.get("okres");
				String obecName = kluby.get("obec");
				String klubName = KLUB_PREFIX + kluby.get("klub");
				
				// create kraj
				try{
					krajOrg = OrganizationLocalServiceUtil.getOrganization(companyId, krajName);
					System.out.print(krajOrg.getName() + " > ");
					sb.append(krajOrg.getName() + " - existuje");
					sb.append("<br />");
				}
				catch(NoSuchOrganizationException e ){
					
					krajOrg = OrganizationLocalServiceUtil.addOrganization(userId, kdhOrgId, krajName, KRAJ, 
							regionId, countryId, statusId, comments, site, serviceContext);
					
					System.out.println("Create KRAJ: " + krajOrg.getName() + " > ");
					sb.append("Create KRAJ: " + krajOrg.getName());
					sb.append("<br />");
					
				}

				
				// create okres
				try{
					okresOrg = OrganizationLocalServiceUtil.getOrganization(companyId, okresName);
					System.out.print(okresOrg.getName() + " > ");
					sb.append("--- OKRES: " + okresOrg.getName() + " - existuje");
					sb.append("<br />");
				}
				catch(NoSuchOrganizationException e ){
				
					okresOrg = OrganizationLocalServiceUtil.addOrganization(userId, krajOrg.getOrganizationId(), okresName, OKRES, 
							regionId, countryId, statusId, comments, site, serviceContext);
					
					System.out.println("Create OKRES: " + okresOrg.getName() + " > ");
					
					sb.append("--- Create OKRES: " + okresOrg.getName());
					sb.append("<br />");
					
				}
				
				// create obec
				try{
					obecOrg = OrganizationLocalServiceUtil.getOrganization(companyId, obecName);
					
						System.out.print(obecOrg.getName() + " > ");
						sb.append("---- --- OBEC: " + obecOrg.getName() +  " - existuje");
						sb.append("<br />");
				}
				catch(NoSuchOrganizationException e ){
					
					obecOrg = OrganizationLocalServiceUtil.addOrganization(userId, okresOrg.getOrganizationId(), obecName, OBEC, 
							regionId, countryId, statusId, comments, site, serviceContext);
					System.out.println("Create OBEC: " + obecOrg.getName() + " > ");
					
					
					sb.append("--- --- Create OBEC: " + obecOrg.getName());
					sb.append("<br />");
					
				}			
				
				// create klub
				try{
					klubOrg = OrganizationLocalServiceUtil.getOrganization(companyId, klubName);
					System.out.print(klubOrg.getName());
					System.out.println("");
					
					sb.append("--- --- --- KLUB: " + klubOrg.getName() + " - existuje");
					sb.append("<br />");	
				}
				catch(NoSuchOrganizationException e ){

					klubOrg = OrganizationLocalServiceUtil.addOrganization(userId, obecOrg.getOrganizationId(), klubName, KLUB,
							regionId, countryId, statusId, comments, site, serviceContext);
					
					System.out.println("Create KLUB: " + klubOrg.getName());
			
					sb.append("--- --- --- Create KLUB: " + klubOrg.getName());
					sb.append("<br />");
					
				}
				
				krajOrg = null;
				okresOrg = null;
				obecOrg = null;
				klubOrg = null;
			}
			
			
			kluby.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			SessionErrors.add(actionRequest, "error");
			
		} catch (IOException e) {
			e.printStackTrace();
			SessionErrors.add(actionRequest, "error");
		}
		
		SessionMessages.add(actionRequest, "export", sb.toString());
		
		actionRequest.setAttribute("export", sb.toString());

	}
 


}
