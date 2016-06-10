package database_daointerfaces;

import java.util.List;

import database_dto.ReceptKompDTO;

public interface ReceptKompDAO {
	ReceptKompDTO getReceptKomp(int receptId, int raavareId) throws DALException;

	List<ReceptKompDTO> getReceptKompList(int receptId) throws DALException;

	List<ReceptKompDTO> getReceptKompList() throws DALException;

	void createReceptKomp(ReceptKompDTO receptkomponent) throws DALException;

	void updateReceptKomp(ReceptKompDTO receptkomponent) throws DALException;
}
