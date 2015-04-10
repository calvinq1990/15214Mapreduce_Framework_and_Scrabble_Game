package edu.cmu.cs.cs214.hw4.core;

/**
 *
 * use enum to store the amount and value of each character
 */

public enum Character {
	a("a",9,1), b("b",2,3), c("c",2,3), d("d",4,2), e("e",12,1), f("f",2,4), g("g",3,2), h("h",2,4), i("i",9,1), j("j",1,8),
	k("k",1,5), l("l",4,1), m("m",2,3), n("n",6,1), o("o",8,1), p("p",2,3), q("q",1,10), r("r",6,1), s("s",4,1), t("t",6,1),
	u("u",4,1), v("v",2,4), w("w",2,4), x("x",1,8), y("y",2,4), z("z",1,10);
	
	private String character;
	private int value;
	private int amount;
	private Character(String character, int amount, int value){
		this.character = character;
		this.amount = amount;
		this.value = value;
		
	}
	public String getCharacter(){
		return this.character;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public int getAmount(){
		return this.amount;
	}
}
