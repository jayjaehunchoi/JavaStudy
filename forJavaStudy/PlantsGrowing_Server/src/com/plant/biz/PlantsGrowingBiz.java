package com.plant.biz;
import static common.JdbcTemplate.*;

import java.sql.Connection;

import com.plant.dao.implement.PlantsGrowingDaoImple;
import com.plant.vo.PlantsGrowingVo;

public class PlantsGrowingBiz {
	
	public void insert_plant_name(PlantsGrowingVo pgVo) {
		Connection conn = getConnection();
		new PlantsGrowingDaoImple(conn).createPlantsId(pgVo);
		close(conn);
		
	}
	
	public void update_plant_species(PlantsGrowingVo pgVo) {
		Connection conn = getConnection();
		new PlantsGrowingDaoImple(conn).updatePlantSpecies(pgVo);
		close(conn);
	}
	
	// 식물 이름 중복 시 false
	public boolean update_plant_name(PlantsGrowingVo pgVo) {
		Connection conn = getConnection();
		boolean check = new PlantsGrowingDaoImple(conn).updatePlantName(pgVo);
		close(conn);
		return check;
	}
	
	public void update_info(PlantsGrowingVo pgVo) {
		Connection conn = getConnection();
		new PlantsGrowingDaoImple(conn).updateAll(pgVo);
		close(conn);
				
	}
	
	public void load_info(PlantsGrowingVo pgVo) {
		Connection conn = getConnection();
		new PlantsGrowingDaoImple(conn).loadInfo(pgVo);
		close(conn);
	}
	
	// true면 이미 게임 기록 존재
	public boolean check_id(PlantsGrowingVo pgVo) {
		Connection conn = getConnection();
		boolean check = new PlantsGrowingDaoImple(conn).checkName(pgVo);
		close(conn);
		return check;
	}
	
}
