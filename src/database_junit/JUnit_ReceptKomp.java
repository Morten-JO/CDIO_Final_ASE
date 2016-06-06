package database_junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import database_daoimpl.MYSQLReceptKompDAO;
import database_daointerfaces.DALException;
import database_dto.ReceptKompDTO;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JUnit_ReceptKomp {

	private static MYSQLReceptKompDAO receptkomp;
	private static ReceptKompDTO receptkompDTO;
	
	@Test
	public void a_getListReceptKomp(){
		receptkomp = new MYSQLReceptKompDAO();
		receptkompDTO = new ReceptKompDTO(3, 2, 20, 0.1);
		try{
			List<ReceptKompDTO> liste = receptkomp.getReceptKompList();
			if(liste == null){
				fail("Gotten list is null!");
			}
			else{
				if(liste.size() == 0){
					fail("Either database is empty, or couldnt get proper list!");
				}
			}
		} catch (DALException e) {
			fail("Could not get list of receptkomps!");
		}
	}
	
	@Test
	public void b_addReceptKomp(){
		try {
			List<ReceptKompDTO> liste = receptkomp.getReceptKompList();
			for(int i = 0; i < liste.size(); i++){
				if(liste.get(i).getRaavareId() == receptkompDTO.getRaavareId()){
					if(liste.get(i).getReceptId() == receptkompDTO.getReceptId()){
						fail("Already exist receptkomp with those and cant add!");
					}
				}
			}
		} catch (DALException e1) {
			fail("failed in getting list for add receptkomp");
		}
		try {
			receptkomp.createReceptKomp(receptkompDTO);
		} catch (DALException e) {
			fail("Could not create receptkomp");
		}
	}

	@Test
	public void c_getReceptKomp() {
		try {
			ReceptKompDTO test = receptkomp.getReceptKomp(receptkompDTO.getReceptId(), receptkompDTO.getRaavareId());
			if(test != null){
				assertEquals(test.getNomNetto(), receptkompDTO.getNomNetto(), 0.1);
			}
			else{
				fail("Gotten receptkomp is null!");
			}
		} catch (DALException e) {
			fail("Could not get receptkomp!");
		}
	}
	
	@Test
	public void d_updateReceptKomp(){
		try {
			receptkompDTO.setNomNetto(2500);
			receptkomp.updateReceptKomp(receptkompDTO);
			ReceptKompDTO temp = receptkomp.getReceptKomp(receptkompDTO.getReceptId(), receptkompDTO.getRaavareId());
			assertEquals(temp.getNomNetto(), receptkompDTO.getNomNetto(), 0.5);
		} catch (DALException e) {
			fail("error getting updating receptkomp!");
		}
	}
}

