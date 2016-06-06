package database_daoimpl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database_connector.Connector;
import database_daointerfaces.DALException;
import database_daointerfaces.RollerDAO;
import database_dto.RollerDTO;

public class MYSQLRollerDAO implements RollerDAO{

	@Override
	public RollerDTO getRolle(int oprId) throws DALException {
		try {
			CallableStatement getOP = (CallableStatement) Connector.getInstance().getConnection().prepareCall("call get_roller(?)");
			getOP.setInt(1, oprId);
			ResultSet rs = getOP.executeQuery();
			if (rs.first()){		
				boolean administrator = rs.getBoolean(2);
			    boolean farmaceut = rs.getBoolean(3);
			    boolean vaerkfoerer = rs.getBoolean(4);
			    
			    RollerDTO newrolle = new RollerDTO(oprId, administrator, farmaceut, vaerkfoerer);
			    return newrolle;
			}
	    } catch (SQLException e) {
	    	throw new DALException(e); 
	    }
	    return null;
	}

	@Override
	public List<RollerDTO> getRolleList() throws DALException {
		List<RollerDTO> list = new ArrayList<RollerDTO>();
		try
		{
			ResultSet rs = Connector.getInstance().doQuery("select * from roller;");
			while (rs.next()) 
			{
				RollerDTO current = new RollerDTO(rs.getInt(1), rs.getBoolean(2), rs.getBoolean(3), rs.getBoolean(4));
				list.add(current);
				 
			}
		} catch (SQLException e) {
			throw new DALException(e); 
		}
		return list;
	}

	@Override
	public void createRolle(RollerDTO rolle) throws DALException {
		try {
			CallableStatement createRolle = (CallableStatement) Connector.getInstance().getConnection().prepareCall("call add_roller(?,?,?,?)");
			createRolle.setInt(1, rolle.getOpr_id());
			createRolle.setBoolean(2, rolle.isAdministrator());
			createRolle.setBoolean(3, rolle.isFarmaceut());
			createRolle.setBoolean(4, rolle.isVaerkfoerer());
			createRolle.execute();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not create roller, check if the database is running!");
		}
	}

	@Override
	public void updateRolle(RollerDTO opr, boolean administrator, boolean farmaceut, boolean vaerkfoerer) {
		try {
			CallableStatement updateRolle = (CallableStatement) Connector.getInstance().getConnection().prepareCall("call update_roller(?,?,?,?)");
			updateRolle.setInt(1, opr.getOpr_id());
			updateRolle.setBoolean(2, administrator);
			updateRolle.setBoolean(3, farmaceut);
			updateRolle.setBoolean(4, vaerkfoerer);
			updateRolle.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
