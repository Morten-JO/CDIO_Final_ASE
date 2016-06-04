package database_junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import database_daoimpl.MYSQLOperatoerDAO;
import database_daoimpl.MYSQLRollerDAO;
import database_daointerfaces.DALException;
import database_dto.OperatoerDTO;
import database_dto.RollerDTO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JUnit_Roller {

	private static MYSQLRollerDAO roller;
	private static MYSQLOperatoerDAO operatoer;
	private static RollerDTO rollerDTO;
	private static OperatoerDTO operatoerDTO; //only used because there must a operatoerDTO with a roller
	
	@Test
	public void a_getListRoller(){
		roller = new MYSQLRollerDAO();
		operatoerDTO = new OperatoerDTO("Morten", "MJ", "121291-1212", "testpass");
		rollerDTO = new RollerDTO(0, true, false, false);
		operatoer = new MYSQLOperatoerDAO();
		try {
			List<RollerDTO> listtest = roller.getRolleList();
			if(listtest == null){
				fail("Gotten list is null!");
			}
			else{
				if(listtest.size() == 0){
					fail("Either database is empty, or couldnt get proper list!");
				}
			}
		} catch (DALException e) {
			fail("Could not get list of roller!");
		}
	}
	
	@Test
	public void b_addOperatoerOgRolle(){
		try {
			operatoer.createOperatoer(operatoerDTO, rollerDTO.isAdministrator(), rollerDTO.isFarmaceut(), rollerDTO.isVaerkfoerer());
			rollerDTO.setOpr_id(operatoerDTO.getOprId());
		} catch (DALException e) {
			fail("Could not createoperatoer");
		}
	}
	
	@Test
	public void c_getRoller() {
		try {
			RollerDTO test = roller.getRolle(rollerDTO.getOpr_id());
			if(test != null){
				if(!(test.isAdministrator() == rollerDTO.isAdministrator())){
					fail("Gotten rolle not equal to DTO");
				}
			}
			else{
				fail("Gotten rolle is null!");
			}
		} catch (DALException e) {
			fail("Could not get rolle!");
		}
	}

	@Test
	public void d_updateRoller(){
		try {
			roller.updateRolle(rollerDTO, false, false, true);
			RollerDTO temp = roller.getRolle(rollerDTO.getOpr_id());
			assertEquals(rollerDTO.isFarmaceut(), temp.isFarmaceut());
		} catch (DALException e) {
			fail("error getting updating roller!");
		}
	}
}