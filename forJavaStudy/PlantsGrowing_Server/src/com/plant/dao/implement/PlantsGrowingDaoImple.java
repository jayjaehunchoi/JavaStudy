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
			
			System.out.printf("데이터 업데이트 완료[id = %1s / plantName = %1s] \n",
							pgVo.getId(), pgVo.getPlants_species() );
			commit(conn);
		} catch(SQLException se) {
			System.out.println("식물 종 업데이트 오류");
			se.printStackTrace();
			rollBack(conn);
		} finally {
			//close(pstmt);
		}
	}
	
	
	// 식물 종류 선택
	public void updatePlantSpecies(PlantsGrowingVo pgVo) {
		
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(plants_growing_update_plants_species);
			pstmt.setInt(1, pgVo.getPlants_species());
			pstmt.setString(2, pgVo.getId());
			pstmt.executeUpdate();
			
			System.out.printf("데이터 업데이트 완료[id = %1s / plantName = %1s] \n",
							pgVo.getId(), pgVo.getPlants_species() );
			commit(conn);
		} catch(SQLException se) {
			System.out.println("식물 종 업데이트 오류");
			se.printStackTrace();
			rollBack(conn);
		} finally {
			close(pstmt);
		}
	}
	
	// 식물이름 업데이트
	public boolean updatePlantName(PlantsGrowingVo pgVo){
		if(!checkDuplicatePlantName(pgVo)) {
			System.out.println("식물 이름 중복");
			return false;
		}
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(plants_growing_update_plants_name);
			pstmt.setString(1, pgVo.getPlants_name());
			pstmt.setString(2, pgVo.getId());
			pstmt.executeUpdate();
			
			System.out.printf("데이터 업데이트 완료[id = %1s / plantName = %1s] \n",
						pgVo.getId(), pgVo.getPlants_name() );
			commit(conn);
		} catch(SQLException e) {
			System.out.println("식물 이름 업데이트 오류");
			e.printStackTrace();
			rollBack(conn);
		}finally {
			close(pstmt);
		}
		return true;
	}
	// 식물이름 중복확인
	// sql문 작성하여 Statement로 보내주고, ResultSet으로 결과 받아줌
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
	
	// 아이디 있으면 true
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

	// 메인페이지에서 아이디가 드러나지 않을때 닉네임으로 세팅하는기능
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
			
			System.out.printf("데이터 업데이트 완료[id = %1s] 물 = %1d 햇빛 = %1d 영양분 = %1d 사랑 = %1d \n",
						pgVo.getId(), pgVo.getPlants_water(), pgVo.getPlants_sun(), pgVo.getPlants_nutrition(), pgVo.getPlants_love());
			commit(conn);
		} catch(SQLException se) {
			System.out.println("데이터 업데이트 오류");
			se.printStackTrace();
			rollBack(conn);
		} finally {
			close(psmt);
		}
		
	}
	
	//데이터 업로드, db의 데이터를 게임 내 정보로 가져오기
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
				

				System.out.printf("[userInfo 불러오기 완료] NAME = %s / PlantName = %s / WATER = %d / SUN = %d / NUTRITION = %d / LOVE = %d / LEVEL = %d / SPECIES = %d\n" ,
						pgVo.getId(), pgVo.getPlants_name(), pgVo.getPlants_water(), pgVo.getPlants_sun(), pgVo.getPlants_nutrition(),pgVo.getPlants_love(),
						pgVo.getPlants_level(), pgVo.getPlants_species());
			}
			
			
		}catch (SQLException e) {
			System.out.println("데이터 불러오기 오류");
			e.printStackTrace();
			rollBack(conn);
		}finally {
			close(rs);
			close(psmt);
		}
	}
	
	
	
}
