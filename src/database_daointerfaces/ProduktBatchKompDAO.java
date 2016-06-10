package database_daointerfaces;

import java.util.List;

import database_dto.ProduktBatchKompDTO;

public interface ProduktBatchKompDAO {
	ProduktBatchKompDTO getProduktBatchKomp(int pbId, int rbId) throws DALException;

	List<ProduktBatchKompDTO> getProduktBatchKompList(int pbId) throws DALException;

	List<ProduktBatchKompDTO> getProduktBatchKompList() throws DALException;

	void createProduktBatchKomp(ProduktBatchKompDTO produktbatchkomponent) throws DALException;

	void updateProduktBatchKomp(ProduktBatchKompDTO produktbatchkomponent) throws DALException;
}
