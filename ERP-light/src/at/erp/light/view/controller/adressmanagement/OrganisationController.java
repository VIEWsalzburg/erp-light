package at.erp.light.view.controller.adressmanagement;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.dto.OrganisationDTO;
import at.erp.light.view.mapper.OrganisationMapper;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.services.IDataBase;
import at.erp.light.view.state.ControllerMessage;

@RestController
public class OrganisationController {

	@Autowired
	private IDataBase dataBaseService;
	
	@RequestMapping(value = "secure/organisation/getAllOrganisations")
	public List<OrganisationDTO> getAllOrganisations() {
		
		List<OrganisationDTO> list = new ArrayList<OrganisationDTO>();

		for (Organisation o : dataBaseService.getAllOrganisations()) {
			list.add(OrganisationMapper.mapToDTO(o));
		}
		return list;
	}
	
	@RequestMapping(value = "/secure/organisation/setOrganisation")
	ControllerMessage setOrganisation(@RequestBody OrganisationDTO organisation, HttpServletRequest request) {
		Organisation entity = OrganisationMapper.mapToEntity(organisation, dataBaseService);
	
		dataBaseService.setOrganisation(entity);
		return new ControllerMessage(true, "Speichern erfoglreich");
	}
	
	@RequestMapping(value = "secure/organisation/getOrganisationById/{id}")
	public OrganisationDTO getOrganisationById(@PathVariable int id) {
		
		List<OrganisationDTO> list = new ArrayList<OrganisationDTO>();

		for (Organisation p : dataBaseService.getAllOrganisations()) {
			list.add(OrganisationMapper.mapToDTO(p));
		}

		for (OrganisationDTO element : list) {
			if (element.getId() == id) {
				return element;
			}
		}

		return null;
	}

	@RequestMapping(value = "secure/organisation/deleteOrganisationById/{id}")
	public ControllerMessage deleteOrganisationById(@PathVariable int id) {

		/*	not implemented in ProdDB by now */
		List<Organisation> oList = dataBaseService.getAllOrganisations();
		List<Organisation> returnList = new ArrayList<Organisation>();

		for (Organisation o : oList) {
			if (o.getOrganisationId() != id) {
				returnList.add(o);
			}
		}

		dataBaseService.setOrganisations(returnList);

		return new ControllerMessage(true, "Löschen erfolgreich!");
	}
	
}
