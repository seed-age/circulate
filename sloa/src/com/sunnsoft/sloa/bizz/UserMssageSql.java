package com.sunnsoft.sloa.bizz;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 使用SQL语句查询用户信息
 * @author chenjian
 *
 */
@Component
public class UserMssageSql {

	@Resource
	private DataSource dataSource;
	
	/**
	 * 1. 查询用户临时存放表, 只要分部名称和分部ID并去重, 得出结果  ---> 分部ID 分部名称
	 * @return
	 */
	public List<Map<String, Object>> getFullNameOrSubcompanyId1() {
		Connection conn = null;
		List<Map<String, Object>> list =  new ArrayList<>();
		try {
			conn = this.dataSource.getConnection();
			String sql = "select DISTINCT full_name,subcompany_id1 from user_mssage_tbl";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			ResultSet result = pstmt.executeQuery();
			while(result.next()) {
				Map<String, Object> map = new HashMap<>();
				String name = result.getString("full_name");
				String id = result.getString("subcompany_id1");
				map.put("fullName", name);
				map.put("subcompanyId1", id);
				list.add(map);
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}finally {
			if(conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
	
	/**
	 * 2. 根据分部ID查询该分部下的部门, 并去重.
	 * @param subcompanyid
	 * @return
	 */
	public List<Map<String, Object>> getDepartmentNameOrId(String subcompanyid){
		List<Map<String, Object>> list =  new ArrayList<>();
		Connection con = null;
		try {
			//获取连接对象
			con = this.dataSource.getConnection();
			//sql语句
			String sql = "select DISTINCT dept_fullname,department_id from user_mssage_tbl WHERE subcompany_id1 = "+subcompanyid;
			PreparedStatement pstmt = con.prepareStatement(sql);
			ResultSet result = pstmt.executeQuery();
			while(result.next()) {
				Map<String, Object> map = new HashMap<>();
				String name = result.getString("dept_fullname");
				String id = result.getString("department_id");
				map.put("deptFullname", name);
				map.put("departmentId", id);
				list.add(map);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return list;
	}
}
