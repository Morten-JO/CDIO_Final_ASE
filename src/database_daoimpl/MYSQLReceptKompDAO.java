package database_daoimpl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database_connector.Connector;
import database_daointerfaces.DALException;
import database_daointerfaces.ReceptKompDAO;
import database_dto.ReceptKompDTO;

public class MYSQLReceptKompDAO implements ReceptKompDAO {

	@Override
	public ReceptKompDTO getReceptKomp(int receptId, int raavareId) throws DALException {
		try {
			CallableStatement getReceptKomponent = (CallableStatement) Connector.getInstance().getConnection()
					.prepareCall("call get_receptkomponent(?,?)");
			getReceptKomponent.setInt(1, receptId);
			getReceptKomponent.setInt(2, raavareId);
			ResultSet rs = getReceptKomponent.executeQuery();
			if (rs.first()) {
				int recept_id = rs.getInt(1);
				int raavare_id = rs.getInt(2);
				double nom_netto = rs.getDouble(3);
				double tolerance = rs.getDouble(4);

				ReceptKompDTO newRecKom = new ReceptKompDTO(recept_id, raavare_id, nom_netto, tolerance);
				return newRecKom;
			}
		} catch (SQLException e) {
			throw new DALException(e);
		}
		return null;
	}

	@Override
	public List<ReceptKompDTO> getReceptKompList(int receptId) throws DALException {
		List<ReceptKompDTO> list = new ArrayList<ReceptKompDTO>();
		try {
			ResultSet rs = Connector.getInstance()
					.doQuery("SELECT * FROM receptkomponent where receptId = " + receptId);
			while (rs.next()) {
				ReceptKompDTO current = new ReceptKompDTO(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4));

				list.add(current);
			}
		} catch (SQLException e) {
			throw new DALException(e);
		}
		return list;
	}

	@Override
	public List<ReceptKompDTO> getReceptKompList() throws DALException {
		List<ReceptKompDTO> list = new ArrayList<ReceptKompDTO>();
		try {
			ResultSet rs = Connector.getInstance().doQuery("SELECT * FROM receptkomponent;");
			while (rs.next()) {
				ReceptKompDTO current = new ReceptKompDTO(rs.getInt(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4));

				list.add(current);
			}
		} catch (SQLException e) {
			throw new DALException(e);
		}
		return list;
	}

	@Override
	public void createReceptKomp(ReceptKompDTO receptkomponent) throws DALException {
		try {
		    CallableStatement createRecept = (CallableStatement) Connector.getInstance().getConnection().prepareCall("call add_receptkomponent(?,?,?,?,?)");
		    createRecept.setInt(1, receptkomponent.getReceptId());
		    createRecept.setInt(2, receptkomponent.getRaavareId());
		    createRecept.setDouble(3, receptkomponent.getNomNetto());
		    createRecept.setDouble(4, receptkomponent.getTolerance());
		    createRecept.setInt(5, 0);
		    createRecept.execute();
		   
		   } catch (Exception e) {
			   e.printStackTrace();
			   System.err.println("Could not create receptkomponent, check if the database is running!");
		  }
	}

	@Override
	public void updateReceptKomp(ReceptKompDTO receptkomponent) throws DALException {
		try {
			Connector.getInstance().doUpdate(
					"UPDATE receptkomponent SET nomNetto =  " + receptkomponent.getNomNetto()
					+ ", tolerance = " + receptkomponent.getTolerance()+ " WHERE receptId = " +
					receptkomponent.getReceptId() +" and raavareId = "+receptkomponent.getRaavareId());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
}