package database_daoimpl;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database_connector.Connector;
import database_daointerfaces.DALException;
import database_daointerfaces.ProduktBatchDAO;
import database_dto.ProduktBatchDTO;

public class MYSQLProduktBatchDAO implements ProduktBatchDAO{

	@Override
	public ProduktBatchDTO getProduktBatch(int pbId) throws DALException {
		try {
			CallableStatement getPB = (CallableStatement) Connector.getInstance().getConnection().prepareCall("call get_produktbatch(?)");
			getPB.setInt(1, pbId);
			ResultSet rs = getPB.executeQuery();
			if (rs.first()){			    	
				int pb_status = rs.getInt(2);
				int pb_recept = rs.getInt(3);
				ProduktBatchDTO newpb = new ProduktBatchDTO(pb_status, pb_recept);
				newpb.setPbId(pbId);
				return newpb;
			}
		} catch (SQLException e) {
			throw new DALException(e); 
		}
		return null;	
	}

	@Override
	public List<ProduktBatchDTO> getProduktBatchList() throws DALException {
		List<ProduktBatchDTO> list = new ArrayList<ProduktBatchDTO>();
		try
		{
			ResultSet rs = Connector.getInstance().doQuery("SELECT * FROM produktbatch;");
			while (rs.next()) 
			{
				ProduktBatchDTO current = new ProduktBatchDTO(rs.getInt(2), rs.getInt(3));
				current.setPbId(rs.getInt(1));
				list.add(current);
			}
		} catch (SQLException e) {
			throw new DALException(e); 
		}
		return list;
	}

	@Override
	public void createProduktBatch(ProduktBatchDTO produktbatch) throws DALException {
		try {
		    CallableStatement createOP = (CallableStatement) Connector.getInstance().getConnection().prepareCall("call add_produktbatch(?,?)");
		    createOP.setInt(1, produktbatch.getStatus());
		    createOP.setInt(2, produktbatch.getReceptId());
		    createOP.execute();
		    
		    ResultSet rs = Connector.getInstance().doQuery("select max(pb_id) from produktbatch;");
			if (rs.first()){
				int id = rs.getInt(1);
				produktbatch.setPbId(id);
			}
		} catch (SQLException e) {
		    System.err.println("Could not create produktbatch, check if the database is running!");
		}
	}

	@Override
	public void updateProduktBatch(ProduktBatchDTO produktbatch) throws DALException {
		try {
			Connector.getInstance().doUpdate(
					"UPDATE produktbatch SET  status = " + produktbatch.getStatus() + "  WHERE pb_id = " +
					produktbatch.getPbId());

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}