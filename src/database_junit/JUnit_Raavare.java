package database_junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import database_daoimpl.MYSQLRaavareDAO;
import database_daointerfaces.DALException;
import database_dto.RaavareDTO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JUnit_Raavare {

	private static MYSQLRaavareDAO raavare;
	private static RaavareDTO raavareDTO;
	
	@Test
	public void a_getListRaavare(){
		raavare = new MYSQLRaavareDAO();
		raavareDTO = new RaavareDTO(8, "Ananas", "DTU");
		try{
			List<RaavareDTO> liste = raavare.getRaavareList();
			if(liste == null){
				fail("Gotten list is null!");
			}
			else{
				if(liste.size() == 0){
					fail("Either database is empty, or couldnt get proper list!");
				}
			}
		} catch (DALException e) {
			fail("Could not get list of raavare!");
		}
	}
	
	@Test
	public void b_addRaavare(){
		try {
			List<RaavareDTO> liste = raavare.getRaavareList();
			for(int i = 0; i < liste.size(); i++){
				if(liste.get(i).getRaavareId() == raavareDTO.getRaavareId()){
					fail("Already exist raavare with those and cant add!");
				}
			}
		} catch (DALException e1) {
			fail("failed in getting list for add raavare");
		}
		try {
			raavare.createRaavare(raavareDTO);
		} catch (DALException e) {
			fail("Could not create raavarebatch");
		}
	}

	@Test
	public void c_getRaavare() {
		try {
			RaavareDTO test = raavare.getRaavare(raavareDTO.getRaavareId());
			if(test != null){
				assertEquals(test.getRaavareNavn(), raavareDTO.getRaavareNavn());
			}
			else{
				fail("Gotten raavare is null!");
			}
		} catch (DALException e) {
			fail("Could not get raavare!");
		}
	}
	
	@Test
	public void d_updateRaavare(){
		try {
			raavareDTO.setRaavareNavn("Banan");
			raavare.updateRaavare(raavareDTO);
			RaavareDTO temp = raavare.getRaavare(raavareDTO.getRaavareId());
			assertEquals(temp.getRaavareNavn(), raavareDTO.getRaavareNavn());
		} catch (DALException e) {
			fail("error getting updating raavare!");
		}
	}
}
