package database_daoimpl;

import java.sql.CallableStatement;
import java.sql.ResultSet;
//import 
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database_connector.Connector;
import database_daointerfaces.DALException;
import database_daointerfaces.OperatoerDAO;
import database_dto.OperatoerDTO;

public class MYSQLOperatoerDAO implements OperatoerDAO {

	public OperatoerDTO getOperatoer(int oprId) throws DALException {
		try {
			CallableStatement getOP = (CallableStatement) Connector.getInstance().getConnection()
					.prepareCall("call get_operatoer(?)");
			getOP.setInt(1, oprId);
			ResultSet rs = getOP.executeQuery();
			if (rs.first()) {
				String opr_navn = rs.getString(2);
				String opr_ini = rs.getString(3);
				String opr_cpr = rs.getString(4);
				String opr_password = rs.getString(5);

				OperatoerDTO newopr = new OperatoerDTO(opr_navn, opr_ini, opr_cpr, opr_password);
				newopr.setOprId(oprId);
				return newopr;
			}
		} catch (SQLException e) {
			throw new DALException(e);
		}
		return null;
	}

	public void createOperatoer(OperatoerDTO opr, boolean administrator, boolean farmaceut, boolean vaerkfoerer)
			throws DALException {
		try {
			int id = 0;
			CallableStatement createOP = (CallableStatement) Connector.getInstance().getConnection()
					.prepareCall("call add_operatoer(?,?,?,?,?,?,?)");
			createOP.setString(1, opr.getOprNavn());
			createOP.setString(2, opr.getIni());
			createOP.setString(3, opr.getCpr());
			createOP.setString(4, opr.getPassword());
			createOP.setBoolean(5, administrator);
			createOP.setBoolean(6, farmaceut);
			createOP.setBoolean(7, vaerkfoerer);
			createOP.execute();
			ResultSet rs = Connector.getInstance().doQuery("select max(oprId) from operatoer;");
			if (rs.first()) {
				id = rs.getInt(1);
				opr.setOprId(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Could not create operatoer, check if the database is running!");
		}
	}

	public void updateOperatoer(OperatoerDTO opr, int id) throws DALException {
		try {
			CallableStatement updateOP = (CallableStatement) Connector.getInstance().getConnection()
					.prepareCall("call update_operatoer(?,?,?,?,?)");
			updateOP.setString(1, opr.getOprNavn());
			updateOP.setString(2, opr.getIni());
			updateOP.setString(3, opr.getCpr());
			updateOP.setString(4, opr.getPassword());
			updateOP.setInt(5, id);
			updateOP.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<OperatoerDTO> getOperatoerList() throws DALException {
		List<OperatoerDTO> list = new ArrayList<OperatoerDTO>();
		try {
			ResultSet rs = Connector.getInstance().doQuery("SELECT * FROM operatoer");
			while (rs.next()) {
				OperatoerDTO current = new OperatoerDTO(rs.getString(2), rs.getString(3), rs.getString(4),
						rs.getString(5));
				current.setOprId(rs.getInt(1));
				list.add(current);

			}
		} catch (SQLException e) {
			throw new DALException(e);
		}
		return list;
	}
}