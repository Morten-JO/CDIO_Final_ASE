package database_junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import database_daoimpl.MYSQLProduktBatchKompDAO;
import database_daointerfaces.DALException;
import database_dto.ProduktBatchKompDTO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JUnit_ProduktBatchKomp {

	private static MYSQLProduktBatchKompDAO produktbatchkomp;
	private static ProduktBatchKompDTO produktbatchkompDTO;
	
	@Test
	public void a_getListProduktBatchKomp(){
		produktbatchkomp = new MYSQLProduktBatchKompDAO();
		produktbatchkompDTO = new ProduktBatchKompDTO(2, 3, 0.5, 100, 3);
		try {
			List<ProduktBatchKompDTO> liste = produktbatchkomp.getProduktBatchKompList();
			if(liste == null){
				fail("Gotten list is null!");
			}
			else{
				if(liste.size() == 0){
					fail("Either database is empty, or couldnt get proper list!");
				}
			}
		} catch (DALException e) {
			fail("Could not get list of produktbatchkomps!");
		}
	}
	
	@Test
	public void b_addProduktBatchKomp(){
		try {
			List<ProduktBatchKompDTO> liste = produktbatchkomp.getProduktBatchKompList();
			for(int i = 0; i < liste.size(); i++){
				if(liste.get(i).getRbId() == produktbatchkompDTO.getRbId()){
					if(liste.get(i).getPbId() == produktbatchkompDTO.getPbId()){
						fail("Already exist produktbatchkomp with those and cant add!");
					}
				}
			}
		} catch (DALException e1) {
			fail("failed in getting list for add produktbatchkomp");
		}
		try {
			produktbatchkomp.createProduktBatchKomp(produktbatchkompDTO);
		} catch (DALException e) {
			fail("Could not create produktbatchkomp");
		}
	}

	@Test
	public void c_getProduktBatchKomp() {
		try {
			ProduktBatchKompDTO test = produktbatchkomp.getProduktBatchKomp(produktbatchkompDTO.getPbId(), produktbatchkompDTO.getRbId());
			if(test != null){
				if(!(test.getRbId() == produktbatchkompDTO.getRbId())){
					fail("Gotten produktbatchkomp not equal to DTO");
				}
			}
			else{
				fail("Gotten produktbatchkomp is null!");
			}
		} catch (DALException e) {
			fail("Could not get produktbatchkomp!");
		}
	}
	
	@Test
	public void d_updateProduktBatchKomp(){
		try {
			produktbatchkompDTO.setNetto(777);
			produktbatchkomp.updateProduktBatchKomp(produktbatchkompDTO);
			ProduktBatchKompDTO temp = produktbatchkomp.getProduktBatchKomp(produktbatchkompDTO.getPbId(), produktbatchkompDTO.getRbId());
			assertEquals(temp.getNetto(), produktbatchkompDTO.getNetto(), 0.1);
		} catch (DALException e) {
			fail("error getting updating produktbatchkomp!");
		}
	}
}
