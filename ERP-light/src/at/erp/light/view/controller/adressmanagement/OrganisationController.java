package at.erp.light.view.controller.adressmanagement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.erp.light.view.dto.OrganisationDTO;
import at.erp.light.view.mapper.OrganisationMapper;
import at.erp.light.view.model.Organisation;
import at.erp.light.view.services.IDataBase;

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

	
}
