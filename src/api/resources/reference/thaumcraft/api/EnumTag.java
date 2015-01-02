package thaumcraft.api;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum EnumTag {
	UNKNOWN		(63,"Obscurus","Unknown, Obscured",1,false,0x282828),
	
	WIND   		(0,"Aura","Air, Wind, Breath",1,false,0xc0c0d7),
	MOTION 		(1,"Motus","Motion, Movement, Speed",1,false,0xcdccf4),
	VOID   		(2,"Vacuos","Empty, Void, Insubstantial",1,true,0xc0c0d7),
	VISION 		(3,"Visum","Sight, Vision, Appearance",3,false,0xd5d4ec),
	KNOWLEDGE	(4,"Cognitio","Learning, Knowledge, Inquiry",1,false,0x8080ee),	
	FLIGHT 		(5,"Volito","Flight, Leap",1,false,0xe7e7d7),
	FIRE		(6,"Ignis","Fire, Heat, Burn",2,true,0xff5a01), 
	DESTRUCTION	(7,"Fractus","Destruction, Fragmented, Shattered",2,true,0x506050),
	LIGHT		(8,"Lux","Light, Brightness, Day",2,false,0xfff663),
	POWER		(9,"Potentia","Power, Energy, Strength",2,true,0xc0ffff),
	MECHANISM	(10,"Machina","Mechanism, Machine, Device",2,false,0x8080a0),
	ROCK		(11,"Saxum","Stone, Rock",4,false,0x5c4842),
	METAL		(12,"Metallum","Metal, Mine, Ore",4,false,0xd8d8d8),
	//13
	EXCHANGE	(14,"Permutatio","Exchange, Change, Barter",4,false,0x578357),
	CLOTH		(15,"Pannus","Cloth, Fabric, Garment, Thread",3,false,0xeaeac2),
	EARTH		(16,"Solum","Earth, Soil, Ground, Foundation",4,false,0x713f2d),
	ARMOR		(17,"Tutamen","Defense, Protection, Security",4,false,0x00c0c0),
	WEAPON		(18,"Telum","Arrow, Sword, Weapon",2,true,0xc05050),
	TOOL		(19,"Instrumentum","Instrument, Tool, Implement",4,false,0x4040ee),
	CRYSTAL  	(20,"Vitreus","Glass, Crystal, Gem, Transparent",1,false,0x80ffff),
	WATER		(21,"Aqua","Water, Fluid",3,false,0x3cd4fc),
	WEATHER		(22,"Aer","Weather, Mist, Climate",3,false,0xc0ffff), 
	COLD		(23,"Gelum","Cold, Ice, Frost",3,true,0xe1ffff), 
	SOUND		(24,"Sonus","Sound, Noise, Din",3,false,0x10c9c0),
	HEAL		(25,"Sano","Heal, Repair, Make Sound",3,false, 0xff8184),
	LIFE		(26,"Victus","Life force, Food, Sustenance",3,false,0xde0005),
	DEATH		(27,"Mortuus","Death, Decay, Undead",6,true,0x404040),
	TRAP		(28,"Vinculum","Bind, Imprison, Trap",4,true,0x9a8080),
	POISON		(29,"Venenum","Poison, Drug, Impure",3,true,0x89f000),
	SPIRIT		(30,"Animus","Soul, Spirit",1,false,0xe0e0e0),
	VALUABLE	(31,"Carus","Expensive, Precious, Valuable",4,false,0xe6be44),
	WOOD		(32,"Lignum","Wood, Forest, Tree",4,false,0x058105),
	FLOWER		(33,"Flos","Flower, Bloom, Blossom",4,false,0xffff40),
	FUNGUS		(34,"Fungus","Mushroom, Toadstool, Fungi",4,false,0xf7e5c7),
	CROP		(35,"Messis","Crops, Harvest",4,false,0xe3ff80),
	PLANT		(36,"Herba","Herb, Plant, Grass",4,false,0x01ac00),
	PURE		(37,"Purus","Pure, Clean, Stainless",3,false,0xa5fffd),
	//38
	//39
	MAGIC		(40,"Praecantatio","Magic, Sorcery",5,false,0x9700c0),
	TIME		(41,"Tempus","Time, Moment, Season",5,false,0x9070e0),
	//42
	//43
	//44
	//45
	//46
	//47
	CONTROL		(48,"Imperito","Control, Command, Dominate",5,false,0x98994b),
	DARK		(49,"Tenebris","Dark, Night, Blindness",5,true,0x252525),
	CRAFT		(50,"Fabrico","Create, Construct, Work",2,false,0x809d80),
	//51
	//52
	BEAST		(53,"Bestia","Animal, Beast",3,true,0x9f6409),
	FLESH		(54,"Corpus","Body, Flesh, Physique",3,false,0xee478d), 
	INSECT		(55,"Bestiola","Spider, Web, Insects",3,false,0x808880),
	EVIL		(56,"Malum","Evil, The Nether, Malice",5,true,0x700000),
	FLUX		(57,"Mutatio","Flux, Chaos",5,true,0xb80bb9),
	ELDRITCH	(58,"Alienis","Eldritch, The End, Strange, Alien",5,true,0x805080)
	//59
	//60
	//61
	//62
	;
	
	// Attributes
	public final int id;
	public final String name;
	public final String meaning;
	
	/**
	 * rough categories for the aspect types. Mostly just used to determine the common aspect type in certain biomes. 
	 */
	public final int element; //1-air, 2-fire, 3-water, 4-earth, 5-mystical, 6-dead/aggressive, 999-deprecated
	
	/**
	 * will wisps spawned from this fluxtype be aggressive?
	 */
	public final boolean aggro; 
	
	public final int color;
    
	private EnumTag(int id, String name, String meaning, int element,boolean aggro, int color ) {
		this.id = id;
		this.name = name;
		this.meaning = meaning;
		this.element = element;
		this.color = color;
		this.aggro = aggro;
		
	}
	
	// Lookup
	private static final Map<Integer,EnumTag> lookup = new HashMap<Integer,EnumTag>();
	
	static { for(EnumTag s : EnumSet.allOf(EnumTag.class)) lookup.put(s.getId(), s); }
	
	public int getId() { return id; }
	
    public static EnumTag get(int id) {
    	if (lookup.get(id)==null||lookup.get(id).element==999)
    		return EnumTag.FLUX;
    	else
    		return lookup.get(id); 
    }

}
