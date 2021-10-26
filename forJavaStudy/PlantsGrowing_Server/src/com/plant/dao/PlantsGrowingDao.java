package com.plant.dao;

public interface PlantsGrowingDao {
	
	public static final String plants_growing_insert_by_id = "insert into plants_growing values(?,?,?,?,?,?,?,?)";
	public static final String plants_growing_select_by_id = "select * from plants_growing where member_id = ?";
	public static final String plants_growing_select_plants_name = "select * from plants_growing where plants_name = ?";
	public static final String plants_growing_update_plants_species = "update plants_growing set plants_species = ? where member_id = ?";
	public static final String plants_growing_update_plants_name = "update plants_growing set plants_name = ? where member_id = ?";
	public static final String plants_growing_update_all = "update plants_growing set plants_water = ?,plants_sun = ?, plants_nutrition = ?, plants_love = ?, plants_level = ? where plants_name = ?";

}
