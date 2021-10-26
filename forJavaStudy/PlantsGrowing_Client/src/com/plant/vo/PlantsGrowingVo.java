package com.plant.vo;

import java.io.Serializable;

public class PlantsGrowingVo implements Serializable {


	@Override
	public String toString() {
		return "PlantsGrowingVo [id=" + id + ", plants_water=" + plants_water + ", plants_sun=" + plants_sun
				+ ", plants_nutrition=" + plants_nutrition + ", plants_love=" + plants_love + ", plants_level="
				+ plants_level + ", plants_species=" + plants_species + ", plants_name=" + plants_name + ", pStatus="
				+ pStatus + "]";
	}

	private static final long serialVersionUID = 1234567890L;
	// JoinColumn(member_id)
	private String id;
	
	// 초기값
	private int plants_water = 0;
	private int plants_sun = 0;
	private int plants_nutrition = 0;
	private int plants_love = 0;
	private int plants_level = 1;
	private int plants_species = 0;
	
	//primary key, 동일 테이블 내에서 중복 불가
	private String plants_name = "";

	public PlantsGrowingVo(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public int getPlants_water() {
		return plants_water;
	}
	
	public void setPlants_water(int plants_water) {
		this.plants_water = plants_water;
	}
	
	public int getPlants_sun() {
		return plants_sun;
	}
	
	public void setPlants_sun(int plants_sun) {
		this.plants_sun = plants_sun;
	}
	
	public int getPlants_nutrition() {
		return plants_nutrition;
	}
	
	public void setPlants_nutrition(int plants_nutrition) {
		this.plants_nutrition = plants_nutrition;
	}
	
	public int getPlants_love() {
		return plants_love;
	}
	
	public void setPlants_love(int plants_love) {
		this.plants_love = plants_love;
	}
	
	public int getPlants_level() {
		return plants_level;
	}
	
	public void setPlants_level(int plants_level) {
		this.plants_level = plants_level;
	}
	
	public int getPlants_species() {
		return plants_species;
	}
	
	public void setPlants_species(int plants_species) {
		this.plants_species = plants_species;
	}
	public String getPlants_name() {
		return plants_name;
	}

	public void setPlants_name(String plants_name) {
		this.plants_name = plants_name;
	}
	
	// 객체 첫 접근, 지속 접근 확인
	private PlantsGrowingStatus pStatus = PlantsGrowingStatus.FIRSTACCES;
	
	public PlantsGrowingStatus getpStatus() {
		return pStatus;
	}

	public void setpStatus(PlantsGrowingStatus pStatus) {
		this.pStatus = pStatus;
	}

}
