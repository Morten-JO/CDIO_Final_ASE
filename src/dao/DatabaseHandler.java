package dao;

import database_connector.Connector;
import database_daoimpl.MYSQLOperatoerDAO;
import database_daoimpl.MYSQLProduktBatchDAO;
import database_daoimpl.MYSQLReceptDAO;
import database_daointerfaces.DALException;
import database_dto.OperatoerDTO;
import database_dto.ProduktBatchDTO;
import database_dto.ReceptDTO;

public class DatabaseHandler {
	
	public String getOperatoerNameFromId(int id){
		MYSQLOperatoerDAO dao = new MYSQLOperatoerDAO();
		OperatoerDTO dto = null;
		try {
			dto = dao.getOperatoer(id);
		} catch (DALException e) {
			e.printStackTrace();
		}
		if(dto != null){
			return dto.getOprNavn();
		}
		else{
			return null;
		}
	}
	
	public String getReceptNameFromProduktBatchId(int id){
		MYSQLProduktBatchDAO dao = new MYSQLProduktBatchDAO();
		ProduktBatchDTO dto = null;
		try {
			dto = dao.getProduktBatch(id);
		} catch (DALException e) {
			e.printStackTrace();
		}
		if(dto != null){
			MYSQLReceptDAO dao2 = new MYSQLReceptDAO();
			ReceptDTO dto2 = null;
			try {
				dto2 = dao2.getRecept(dto.getReceptId());
			} catch (DALException e) {
				e.printStackTrace();
			}
			if(dto2 != null){
				return dto2.getReceptNavn();
			}
			else{
				return null;
			}
		}
		else{
			return null;
		}
	}
	
	public void setProduktBatchStatus(int id, int status){
		MYSQLProduktBatchDAO dao = new MYSQLProduktBatchDAO();
		ProduktBatchDTO dto = null;
		try {
			dto = dao.getProduktBatch(id);
		} catch (DALException e){
			e.printStackTrace();
		}
		if(dto != null){
			dto.setStatus(status);
			try {
				dao.updateProduktBatch(dto);
			} catch (DALException e) {
				e.printStackTrace();
			}
		}
	}
	
	public String[] getRaavareForProduktBatch(int id){
		return null;
	}
	
}
