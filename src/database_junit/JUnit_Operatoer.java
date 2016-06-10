package database_junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import database_daoimpl.MYSQLOperatoerDAO;
import database_daointerfaces.DALException;
import database_dto.OperatoerDTO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JUnit_Operatoer {

	private static MYSQLOperatoerDAO operatoer;
	private static OperatoerDTO operatoerDTO;

	@Test
	public void a_getListOperatoer() {
		operatoer = new MYSQLOperatoerDAO();
		operatoerDTO = new OperatoerDTO("Morten", "MJ", "121291-1212", "testpass");
		try {
			List<OperatoerDTO> listtest = operatoer.getOperatoerList();
			if (listtest == null) {
				fail("Gotten list is null!");
			} else {
				if (listtest.size() == 0) {
					fail("Either database is empty, or couldnt get proper list!");
				}
			}
		} catch (DALException e) {
			fail("Could not get list of operatoers!");
		}
	}

	@Test
	public void b_addOperatoer() {
		try {
			List<OperatoerDTO> listTest = operatoer.getOperatoerList();
			for (int i = 0; i < listTest.size(); i++) {
				if (listTest.get(i).getOprNavn().equals(operatoerDTO.getOprNavn())) {
					if (listTest.get(i).getCpr().equals(operatoerDTO.getCpr())) {
						fail("Already exist operatoer with those and cant add!");
					}
				}
			}
		} catch (DALException e1) {
			fail("failed in getting list for addoperatoer");
		}
		try {
			operatoer.createOperatoer(operatoerDTO, true, false, false);
		} catch (DALException e) {
			fail("Could not createoperatoer");
		}
	}

	@Test
	public void c_getOperatoer() {
		try {
			OperatoerDTO test = operatoer.getOperatoer(operatoerDTO.getOprId());
			if (test != null) {
				if (!test.getOprNavn().equals(operatoerDTO.getOprNavn())) {
					fail("Gotten operatoer not equal to DTO");
				}
			} else {
				fail("Gotten operatoer is null!");
			}
		} catch (DALException e) {
			fail("Could not get operatoer!");
		}
	}

	@Test
	public void d_updateOperatoer() {
		OperatoerDTO testSecond = new OperatoerDTO("Mortin", "MJ", "131292-1313", "testpass");
		try {
			operatoer.updateOperatoer(testSecond, operatoerDTO.getOprId());
			OperatoerDTO temp = operatoer.getOperatoer(operatoerDTO.getOprId());
			assertEquals(testSecond.getCpr(), temp.getCpr());
		} catch (DALException e) {
			fail("error getting updating operatoer!");
		}
	}
}