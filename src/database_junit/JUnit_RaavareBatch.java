package database_junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import database_daoimpl.MYSQLRaavareBatchDAO;
import database_daointerfaces.DALException;
import database_dto.RaavareBatchDTO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JUnit_RaavareBatch {

	private static MYSQLRaavareBatchDAO raavarebatch;
	private static RaavareBatchDTO raavarebatchDTO;

	@Test
	public void a_getListRaavareBatch() {
		raavarebatch = new MYSQLRaavareBatchDAO();
		raavarebatchDTO = new RaavareBatchDTO(8, 7, 100);
		try {
			List<RaavareBatchDTO> liste = raavarebatch.getRaavareBatchList();
			if (liste == null) {
				fail("Gotten list is null!");
			} else {
				if (liste.size() == 0) {
					fail("Either database is empty, or couldnt get proper list!");
				}
			}
		} catch (DALException e) {
			fail("Could not get list of raavarebatches!");
		}
	}

	@Test
	public void b_addRaavareBatch() {
		try {
			List<RaavareBatchDTO> liste = raavarebatch.getRaavareBatchList();
			for (int i = 0; i < liste.size(); i++) {
				if (liste.get(i).getRbId() == raavarebatchDTO.getRbId()) {
					fail("Already exist raavarebatch with those and cant add!");
				}
			}
		} catch (DALException e1) {
			fail("failed in getting list for add raavarebatch");
		}
		try {
			raavarebatch.createRaavareBatch(raavarebatchDTO);
		} catch (DALException e) {
			fail("Could not create raavarebatch");
		}
	}

	@Test
	public void c_getRaavareBatch() {
		try {
			RaavareBatchDTO test = raavarebatch.getRaavareBatch(raavarebatchDTO.getRbId());
			if (test != null) {
				assertEquals(test.getMaengde(), raavarebatchDTO.getMaengde(), 0.1);
			} else {
				fail("Gotten raavarebatch is null!");
			}
		} catch (DALException e) {
			fail("Could not get raavarebatch!");
		}
	}

	@Test
	public void d_updateRaavareBatch() {
		try {
			raavarebatchDTO.setMaengde(5000);
			raavarebatch.updateRaavareBatch(raavarebatchDTO);
			RaavareBatchDTO temp = raavarebatch.getRaavareBatch(raavarebatchDTO.getRbId());
			assertEquals(temp.getMaengde(), raavarebatchDTO.getMaengde(), 0.5);
		} catch (DALException e) {
			fail("error getting updating raavarebatch!");
		}
	}
}
