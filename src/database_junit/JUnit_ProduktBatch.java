package database_junit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import database_daoimpl.MYSQLProduktBatchDAO;
import database_daointerfaces.DALException;
import database_dto.ProduktBatchDTO;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JUnit_ProduktBatch {

	private static MYSQLProduktBatchDAO produktbatch;
	private static ProduktBatchDTO produktbatchDTO;

	@Test
	public void a_getListProduktbatch() {
		produktbatch = new MYSQLProduktBatchDAO();
		produktbatchDTO = new ProduktBatchDTO(3, 2);
		try {
			List<ProduktBatchDTO> liste = produktbatch.getProduktBatchList();
			if (liste == null) {
				fail("Gotten list is null!");
			} else {
				if (liste.size() == 0) {
					fail("Either database is empty, or couldnt get proper list!");
				}
			}
		} catch (DALException e) {
			fail("Could not get list of produktbatchs!");
		}
	}

	@Test
	public void b_addProduktbatch() {
		try {
			List<ProduktBatchDTO> liste = produktbatch.getProduktBatchList();
			for (int i = 0; i < liste.size(); i++) {
				if (liste.get(i).getStatus() == produktbatchDTO.getStatus()) {
					if (liste.get(i).getReceptId() == produktbatchDTO.getReceptId()) {
						fail("Already exist produktbatch with those and cant add!");
					}
				}
			}
		} catch (DALException e1) {
			fail("failed in getting list for add produktbatch");
		}
		try {
			produktbatch.createProduktBatch(produktbatchDTO);
		} catch (DALException e) {
			fail("Could not create produktbatch");
		}
	}

	@Test
	public void c_getProduktbatch() {
		try {
			ProduktBatchDTO test = produktbatch.getProduktBatch(produktbatchDTO.getPbId());
			if (test != null) {
				if (!(test.getStatus() == produktbatchDTO.getStatus())) {
					fail("Gotten produktbatch not equal to DTO");
				}
			} else {
				fail("Gotten produktbatch is null!");
			}
		} catch (DALException e) {
			fail("Could not get produktbatch!");
		}
	}

	@Test
	public void d_updateProduktbatch() {
		try {
			produktbatchDTO.setStatus(10);
			produktbatch.updateProduktBatch(produktbatchDTO);
			ProduktBatchDTO temp = produktbatch.getProduktBatch(produktbatchDTO.getPbId());
			assertEquals(temp.getStatus(), 10);
		} catch (DALException e) {
			fail("error getting updating produktbatch!");
		}
	}
}