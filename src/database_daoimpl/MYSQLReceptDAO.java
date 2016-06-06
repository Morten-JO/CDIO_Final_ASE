package database_daoimpl;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database_connector.Connector;
import database_daointerfaces.DALException;
import database_daointerfaces.ReceptDAO;
import database_dto.ReceptDTO;

public class MYSQLReceptDAO implements ReceptDAO{
	
	@Override
	public ReceptDTO getRecept(int receptId) throws DALException {
		try {
			CallableStatement getRecept = (CallableStatement) Connector.getInstance().getConnection().prepareCall("call get_recept(?)");
			getRecept.setInt(1, receptId);
			ResultSet rs = getRecept.executeQuery();
			if (rs.first()){			    	
				String recept_navn = rs.getString(2);
				ReceptDTO newRec = new ReceptDTO(recept_navn);
				newRec.setReceptId(receptId);
				return newRec;
			}
		}
		catch (SQLException e) {
			throw new DALException(e); 
		}
		return null;
	}
	
	@Override
	public List<ReceptDTO> getReceptList() throws DALException {
		List<ReceptDTO> list = new ArrayList<ReceptDTO>();
		try
		{
			ResultSet rs = Connector.getInstance().doQuery("SELECT * FROM recept;");
			while (rs.next()) 
			{
				ReceptDTO current = new ReceptDTO(rs.getString(2));
				current.setReceptId(rs.getInt(1));
				list.add(current);
			}
		}
		catch (SQLException e) {
			throw new DALException(e); 
		}
		return list;
	}
	
	@Override
	public void createRecept(ReceptDTO recept) throws DALException {
		try {
			int id = 0;
		    CallableStatement createRecept = (CallableStatement) Connector.getInstance().getConnection().prepareCall("call add_recept(?)");
			createRecept.setString(1, recept.getReceptNavn());	   
			createRecept.execute();  
			ResultSet rs = Connector.getInstance().doQuery("select max(recept_id) from recept;");
			if (rs.first()){   
				id = rs.getInt(1);		
			}
			recept.setReceptId(id);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not create recept, check if the database is running!");
		}
	}

	@Override
	public void updateRecept(ReceptDTO recept) throws DALException {
		try {
			Connector.getInstance().doUpdate(
					"UPDATE recept SET  recept_navn= '" + recept.getReceptNavn() + "' WHERE recept_id = " +
					recept.getReceptId());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ResultSet getFullRecept(String navn) {
		try {
			ResultSet getFullRecept = Connector.getInstance().doQuery("select * from view_fuldrecept where recept_navn = '"+navn+"';");
			if (getFullRecept.first()){			    	
				return getFullRecept;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
