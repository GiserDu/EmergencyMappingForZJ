package telecarto.data.util;

public class ChEnConverter {
	
	/**
	 * 中文表名转换成英文
	 * @param chStr
	 * @return
	 */
	public static String convertTableNameCH2En(String chStr){
		if(chStr != null)
		{
			switch(chStr){
                case "人口与就业":
                    return "theme_pop";
                case "国民经济":
                    return "theme_eco";
                case "植被覆盖":
                    return "theme_vege";
                case "水域":
                    return "theme_water";
                case "地形地貌":
                    return "theme_terrain";
                case "荒漠与裸露地表":
                    return "theme_desert";
                case "交通网络":
                    return "theme_traffic";
                case "居民地与设施":
                    return "theme_resd";
			}
		}
		return null;
	}

	/**
	 * 英文表名转换成中文
	 * @param enStr
	 * @return
	 */
	public static String convertTableNameEn2CH(String enStr){
		if(enStr != null)
		{
			switch(enStr){
			case "SMJCB":
				return "水面基础表";
			case "SYJCB":
				return "水域基础表";
			case "SGSSJCB":
				return "水工设施基础表";
			case "FWFX1":
				return "人口分析";
			}
		}
		return null;
	}
	
	
	/**
	 * 中文指标名转换成英文
	 * @param chStr
	 * @return
	 */
	public static String covertColNameCh2En(String chStr){
		if(chStr != null)
		{
			switch(chStr){
			case "常住人口":
				return "POP_PERM";
			case "户籍人口":
				return "POP_TOTAL";
			case "男性人口":
				return "POP_MAN";
			case "女性人口":
				return "POP_WOMAN";
//			case "水渠":
//				return "SQ";
			}
		}
		return null;
	}
	
	
	/**
	 * 英文指标转换成中文
	 * @param enStr
	 * @return
	 */
	public static String convertColNameEn2Ch(String enStr){
		if(enStr != null)
		{
			switch(enStr){
			case "SMMJB":
				return "水域";
			case "SM":
				return "水面";
			case "SQ":
				return "水渠";
			case "BCYCNJX":
				return "冰川与常年积雪";
			case "BC":
				return "冰川";
			case "CNJX":
				return "常年积雪";
				
				
			case "HQ_M":
				return "河渠（面）";
			case "HQ_X":
				return "河渠（线）";
			case "HL_M":
				return "河流（面）";
			case "HL_X":
				return "河流（线）";
			case "SQ_M":
				return "水渠（面）";
			case "SQ_X":
				return "水渠（线）";
			case "HP_X":
				return "湖泊（面）";
			case "KUT_M":
				return "库塘（面）";
			case "SK_M":
				return "水库（面）";
			case "KET_M":
				return "坑塘（面）";
				
			case "SGSS":
				return "水工设施";
			case "DB":
				return "堤坝";
			case "Z":
				return "闸";
			case "PGBZ":
				return "排管泵站";
			case "YHD":
				return "溢洪道";
			case "QTSGGZW":
				return "其他水工建筑";
				
			case "HLSL":
				return "人均收入";
			case "HZSL":
				return "人均绿地面积";
			case "FBSL":
				return "人均水资源";
			case "TFSL":
				return "人均交通资源";
			case "DZSL":
				return "人均消费";
			case "DWXZSL":
				return "消费水平";
			}
		}
		return null;
	}
}
