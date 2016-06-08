package dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database_connector.Connector;
import database_daoimpl.MYSQLOperatoerDAO;
import database_daoimpl.MYSQLProduktBatchDAO;
import database_daoimpl.MYSQLProduktBatchKompDAO;
import database_daoimpl.MYSQLRaavareBatchDAO;
import database_daoimpl.MYSQLReceptDAO;
import database_daoimpl.MYSQLReceptKompDAO;
import database_daointerfaces.DALException;
import database_daointerfaces.ProduktBatchKompDAO;
import database_dto.OperatoerDTO;
import database_dto.ProduktBatchDTO;
import database_dto.ProduktBatchKompDTO;
import database_dto.RaavareBatchDTO;
import database_dto.ReceptDTO;
import database_dto.ReceptKompDTO;

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
				return "";
			}
		}
		else{
			return "";
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
		try {
			ResultSet set = Connector.getInstance().doQuery("select * from raavare natural join receptkomponent where receptId = "+id+";");
			ArrayList<String> list = new ArrayList<String>();
			while (set.next()) 
			{
				list.add(set.getString(2));
				 
			}
			String[] strs = new String[list.size()];
			strs = list.toArray(strs);
			return strs;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Double getMaengdeFromRaavareBatchId(int id){
		MYSQLRaavareBatchDAO dao = new MYSQLRaavareBatchDAO();
		RaavareBatchDTO dto = null;
		try {
			dto = dao.getRaavareBatch(id);
			return dto.getMaengde();
		} catch (DALException e) {
			e.printStackTrace();
		}
		return 0.0;
	}
	
	public int getReceptIdFromProduktBatchId(int id){
		MYSQLProduktBatchDAO dao = new MYSQLProduktBatchDAO();
		ProduktBatchDTO dto = null;
		try {
			dto = dao.getProduktBatch(id);
			return dto.getReceptId();
		} catch (DALException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean updateProduktBatchKomponent(int pbId, int rbId, double tara, double netto, int oprId){
		MYSQLProduktBatchKompDAO dao = new MYSQLProduktBatchKompDAO();
		try {
			ProduktBatchKompDTO dto = dao.getProduktBatchKomp(pbId, rbId);
			dto.setTara(tara);
			dto.setNetto(netto);
			dto.setOprId(oprId);
			dao.updateProduktBatchKomp(dto);
			return true;
		} catch (DALException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean removeNettoFromRaavareBatch(int rbId, double amount){
		MYSQLRaavareBatchDAO dao = new MYSQLRaavareBatchDAO();
		try {
			RaavareBatchDTO dto = dao.getRaavareBatch(rbId);
			dto.setMaengde(dto.getMaengde()-amount);
			dao.updateRaavareBatch(dto);
			return true;
		} catch (DALException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean hasEnoughFromRaavareBatch(int rbId, double amount){
		MYSQLRaavareBatchDAO dao = new MYSQLRaavareBatchDAO();
		try {
			RaavareBatchDTO dto = dao.getRaavareBatch(rbId);
			if(dto.getMaengde() >= amount){
				return true;
			}
		} catch (DALException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public double getMaengdeFromRBIDandPBID(int rbId, int pbId){
		MYSQLRaavareBatchDAO rbDAO = new MYSQLRaavareBatchDAO();
		MYSQLProduktBatchDAO pbDAO = new MYSQLProduktBatchDAO();
		MYSQLReceptKompDAO rkDAO = new MYSQLReceptKompDAO();
		try {
			RaavareBatchDTO rbDTO = rbDAO.getRaavareBatch(rbId);
			ProduktBatchDTO pbDTO = pbDAO.getProduktBatch(pbId);
			ReceptKompDTO rkDTO = rkDAO.getReceptKomp(pbDTO.getReceptId(), rbDTO.getRaavareId());
			return rkDTO.getNomNetto();
		} catch (DALException e) {
			e.printStackTrace();
		}
		return 0.0;
	}
	
	public double getToleranceFromRBIDandPBID(int rbId, int pbId){
		MYSQLRaavareBatchDAO rbDAO = new MYSQLRaavareBatchDAO();
		MYSQLProduktBatchDAO pbDAO = new MYSQLProduktBatchDAO();
		MYSQLReceptKompDAO rkDAO = new MYSQLReceptKompDAO();
		try {
			RaavareBatchDTO rbDTO = rbDAO.getRaavareBatch(rbId);
			ProduktBatchDTO pbDTO = pbDAO.getProduktBatch(pbId);
			ReceptKompDTO rkDTO = rkDAO.getReceptKomp(pbDTO.getReceptId(), rbDTO.getRaavareId());
			return rkDTO.getTolerance();
		} catch (DALException e) {
			e.printStackTrace();
		}
		return 0.0;
	}
}
