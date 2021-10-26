package com.plant.dao.implement;

import static common.JdbcTemplate.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.plant.dao.PlantsGrowingDao;
import com.plant.vo.MemberVo;
import com.plant.vo.PlantsGrowingVo;

public class PlantsGrowingDaoImple implements PlantsGrowingDao{
	Connection conn;
	
	
	
	public PlantsGrowingDaoImple(Connection conn) {
		this.conn = conn;
	}


	public void createPlantsId(PlantsGrowingVo pgVo) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(plants_growing_insert_by_id);
			
			pstmt.setString(1, pgVo.getId());
			pstmt.setInt(2, pgVo.getPlants_water());
			pstmt.setInt(3, pgVo.getPlants_sun());
			pstmt.setInt(4, pgVo.getPlants_nutrition());
			pstmt.setInt(5, pgVo.getPlants_love());
			pstmt.setInt(6, pgVo.getPlants_level());
			pstmt.setInt(7, pgVo.getPlants_species());
			pstmt.setString(8, pgVo.getPlants_name());;
			pstmt.executeUpdate();
			
			System.out.printf("������ ������Ʈ �Ϸ�[id = %1s / plantName = %1s] \n",
							pgVo.getId(), pgVo.getPlants_species() );
			commit(conn);
		} catch(SQLException se) {
			System.out.println("�Ĺ� �� ������Ʈ ����");
			se.printStackTrace();
			rollBack(conn);
		} finally {
			//close(pstmt);
		}
	}
	
	
	// �Ĺ� ���� ����
	public void updatePlantSpecies(PlantsGrowingVo pgVo) {
		
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(plants_growing_update_plants_species);
			pstmt.setInt(1, pgVo.getPlants_species());
			pstmt.setString(2, pgVo.getId());
			pstmt.executeUpdate();
			
			System.out.printf("������ ������Ʈ �Ϸ�[id = %1s / plantName = %1s] \n",
							pgVo.getId(), pgVo.getPlants_species() );
			commit(conn);
		} catch(SQLException se) {
			System.out.println("�Ĺ� �� ������Ʈ ����");
			se.printStackTrace();
			rollBack(conn);
		} finally {
			close(pstmt);
		}
	}
	
	// �Ĺ��̸� ������Ʈ
	public boolean updatePlantName(PlantsGrowingVo pgVo){
		if(!checkDuplicatePlantName(pgVo)) {
			System.out.println("�Ĺ� �̸� �ߺ�");
			return false;
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(plants_growing_update_plants_name);
			pstmt.setString(1, pgVo.getPlants_name());
			pstmt.setString(2, pgVo.getId());
			pstmt.executeUpdate();
			
			System.out.printf("������ ������Ʈ �Ϸ�[id = %1s / plantName = %1s] \n",
						pgVo.getId(), pgVo.getPlants_name() );
			commit(conn);
		} catch(SQLException e) {
			System.out.println("�Ĺ� �̸� ������Ʈ ����");
			e.printStackTrace();
			rollBack(conn);
		}finally {
			close(pstmt);
		}
		return true;
	}
	// �Ĺ��̸� �ߺ�Ȯ��
	// sql�� �ۼ��Ͽ� Statement�� �����ְ�, ResultSet���� ��� �޾���
	public boolean checkDuplicatePlantName(PlantsGrowingVo pgVo) {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(plants_growing_select_plants_name);
			pstmt.setString(1, pgVo.getPlants_name());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();

		}finally {
			close(rs);
			close(pstmt);
		}
				
		return true;
	}
	
	// ���̵� ������ true
	public boolean checkName(PlantsGrowingVo pgVo) {
		
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = conn.prepareStatement(plants_growing_select_by_id);
			pstmt.setString(1, pgVo.getId());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				return true;
			}
		}catch(SQLException e) {
			e.printStackTrace();

		}finally {
			close(rs);
			close(pstmt);
		}
				
		return false;
	}

	// �������������� ���̵� �巯���� ������ �г������� �����ϴ±��
	public void updateAll(PlantsGrowingVo pgVo) {
		PreparedStatement psmt = null;
		
		try {
			psmt = conn.prepareStatement(plants_growing_update_all);
			psmt.setInt(1, pgVo.getPlants_water());
			psmt.setInt(2, pgVo.getPlants_sun());
			psmt.setInt(3, pgVo.getPlants_nutrition());
			psmt.setInt(4, pgVo.getPlants_love());
			psmt.setInt(5, pgVo.getPlants_level());
			psmt.setString(6, pgVo.getPlants_name());
			psmt.executeUpdate();
			
			System.out.printf("������ ������Ʈ �Ϸ�[id = %1s] �� = %1d �޺� = %1d ����� = %1d ��� = %1d \n",
						pgVo.getId(), pgVo.getPlants_water(), pgVo.getPlants_sun(), pgVo.getPlants_nutrition(), pgVo.getPlants_love());
			commit(conn);
		} catch(SQLException se) {
			System.out.println("������ ������Ʈ ����");
			se.printStackTrace();
			rollBack(conn);
		} finally {
			close(psmt);
		}
		
	}
	
	//������ ���ε�, db�� �����͸� ���� �� ������ ��������
	public void loadInfo(PlantsGrowingVo pgVo) {
		PreparedStatement psmt = null;
		ResultSet rs = null;
		
		try {
			psmt = conn.prepareStatement(plants_growing_select_plants_name);
			psmt.setString(1, pgVo.getPlants_name());
			rs = psmt.executeQuery();
			if(rs.next()) {
				
				pgVo.setPlants_water(rs.getInt(2));
				pgVo.setPlants_sun(rs.getInt(3));
				pgVo.setPlants_nutrition(rs.getInt(4));
				pgVo.setPlants_love(rs.getInt(5));
				pgVo.setPlants_level(rs.getInt(6));
				pgVo.setPlants_species(rs.getInt(7));
				pgVo.setPlants_name(rs.getString(8));
				

				System.out.printf("[userInfo �ҷ����� �Ϸ�] NAME = %s / PlantName = %s / WATER = %d / SUN = %d / NUTRITION = %d / LOVE = %d / LEVEL = %d / SPECIES = %d\n" ,
						pgVo.getId(), pgVo.getPlants_name(), pgVo.getPlants_water(), pgVo.getPlants_sun(), pgVo.getPlants_nutrition(),pgVo.getPlants_love(),
						pgVo.getPlants_level(), pgVo.getPlants_species());
			}
			
			
		}catch (SQLException e) {
			System.out.println("������ �ҷ����� ����");
			e.printStackTrace();
			rollBack(conn);
		}finally {
			close(rs);
			close(psmt);
		}
	}
	
	
	
}
