package com.koez7.facts;

public class FactContainer {
	private static Fact[] fact;
	
	public static void initFact(int totalFact){
		fact = new Fact[totalFact];
	}
	
	public static Fact[] getFact(){
		return fact;
	}
	
	public static Fact getFact(int index){
		return fact[index];
	}
	
	public static void setFact(int index, String title, String desc, String imgRef){
		fact[index] = new Fact();
		fact[index].setTitle(title);
		fact[index].setDescription(desc);
		fact[index].setImageHref(imgRef);
	}
	
	public static String getTitle(int index){
		return fact[index].getTitle();
	}
	
	public static String getDesc(int index){
		return fact[index].getDescription();
	}
	
	public static String getImgRef(int index){
		return fact[index].getImageHref();
	}
}
