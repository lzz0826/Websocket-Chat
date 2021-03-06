package tw.tony.com.model;



public class Permission {

	
	private int permission_id;             // 權限id
	
	private String permission_name;        // 權限名稱
	
	private String permission_description; // 權限描述

	public int getPermission_id() {
		return permission_id;
	}

	public void setPermission_id(int permission_id) {
		this.permission_id = permission_id;
	}

	public String getPermission_name() {
		return permission_name;
	}

	public void setPermission_name(String permission_name) {
		this.permission_name = permission_name;
	}

	public String getPermission_description() {
		return permission_description;
	}

	public void setPermission_description(String permission_description) {
		this.permission_description = permission_description;
	}
	
    @Override
    public String toString() {
        return "Permission{" +
                "permission_id=" + permission_id +
                ", permission_name='" + permission_name + '\'' +
                ", permission_description='" + permission_description + '\'' +
                '}';
    }
	
	
}
