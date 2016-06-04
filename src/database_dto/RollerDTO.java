package database_dto;

public class RollerDTO {

	private int opr_id;
	private boolean administrator;
	private boolean farmaceut;
	private boolean vaerkfoerer;
	
	public RollerDTO(int opr_id, boolean administrator, boolean farmaceut, boolean vaerkfoerer){
		this.opr_id = opr_id;
		this.administrator = administrator;
		this.farmaceut = farmaceut;
		this.vaerkfoerer = vaerkfoerer;
	}
	
	public RollerDTO(RollerDTO rolle){
		this.opr_id = rolle.getOpr_id();
		this.administrator = rolle.isAdministrator();
		this.farmaceut = rolle.isFarmaceut();
		this.vaerkfoerer = rolle.isVaerkfoerer();
	}

	public int getOpr_id() {
		return opr_id;
	}

	public void setOpr_id(int opr_id) {
		this.opr_id = opr_id;
	}

	public boolean isAdministrator() {
		return administrator;
	}

	public void setAdministrator(boolean administrator) {
		this.administrator = administrator;
	}

	public boolean isFarmaceut() {
		return farmaceut;
	}

	public void setFarmaceut(boolean farmaceut) {
		this.farmaceut = farmaceut;
	}

	public boolean isVaerkfoerer() {
		return vaerkfoerer;
	}

	public void setVaerkfoerer(boolean vaerkfoerer) {
		this.vaerkfoerer = vaerkfoerer;
	}
	
	
	
}
