package com.sample;

import java.util.Date;
import org.kie.api.KieServices;
import org.kie.api.logger.KieRuntimeLogger;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import java.text.SimpleDateFormat;

public class Car_Rental {
	public static SimpleDateFormat date_format = new SimpleDateFormat("dd MM yyyy");
	public static class Category {
			private char name;
			private char next;
			private int more_than_3d;
			private int less_than_3d;
			private int chair;
			private int bicycle_carrier;

        		public int get_more_than_3d() {
        			return this.more_than_3d;
        		}
        		public int get_less_than_3d() {
        			return this.less_than_3d;
        		}
			public char get_name() {
				return this.name;
			}
			public char get_next() {
				return this.next;
			}
			public char get_chair() {
				return this.chair;
			}
			public char get_bicycle_carrier() {
				return this.bicycle_carrier;
			}

			public Category(char name, char next, int m,int n,int o,int p) {
				this.name=name;
				this.next=next;
				this.more_than_3d=m;
				this.less_than_3d=n;
				this.chair=o;
				this.bicycle_carrier=p;
			}
	}

	public static class Rent_Date {
			private String which;
			private Date fromm;
			private Date to;
			
			public String get_which() {
				return this.which;
			}
			public Date get_from() {
				return this.fromm;
			}
			public Date get_to() {
				return this.to;
			}
			public boolean overlapping(Date a, Date b) {
				return this.fromm.getTime() <= b.getTime() && a.getTime() <= this.to.getTime();
				
			}
			public Rent_Date(String w, Date f, Date t) {
				this.which = w;
				this.fromm = f;
				this.to = t;
			}
	}
	public static class Car {
			private String id;
			private char category;
	        
	       		public String get_id() {
	        		return this.id;
	        	}
	        	public char get_category() {
	        		return this.category;
	        	}

	        	public Car(String id, char cat) {
	        		this.id = id;
	                	this.category=cat;     
	        	}  
	}
	
	public static class Client {
			private String name;
			private boolean member;

        		public String get_name() {
        			return this.name;
        		}
        	
        		public boolean get_member() {
        			return this.member;
        		}
        	
			public Client(String name, boolean mem) {
				this.name = name;
				this.member=mem;
			}
	}
	
	public static class Rent {
		//included in constructor
		private Client client; 
		private Date from_date;
		private Date to_date;
		private char pref_cat;
		private int chair;
		private int bicycle_carrier;
		private long period;
		
		//other attributes
		private boolean date_correct; 
		private int add_costs;
		private int day_cost;
		private double km_cost;
		private boolean has_car;
		private Car car;
		
		//getters for attributes from constructor
		public Client get_client() {
			return this.client;
		}
		public Date get_from() {
			return this.from_date;
		}
		public Date get_to() {
			return this.to_date;
		}
		public char get_pref_cat() {
			return this.pref_cat;
		}
		public long get_period() {
			return this.period;
		}
		//getters and setters for other attributes
		public boolean get_date_correct() {
			return this.date_correct;
		}
		public void set_date_correct(boolean v) {
			this.date_correct = v;
		}
		public int get_add_costs() {
			return this.add_costs;
		}
		public void set_add_costs(int c,int b,boolean mem) {
			int i = (mem) ? 1 : 0;
			this.add_costs=Math.max(this.chair*c+this.bicycle_carrier*b-i*10,0);
		}
		public int get_day_cost() {
			return this.day_cost;
		}
		public void set_day_cost(int v) {
			this.day_cost = v;
		}
		public double get_km_cost() {
			return this.km_cost;
		}
		public void set_km_cost(double v) {
			this.km_cost=v;
		}
		public boolean get_has_car() {
			return this.has_car;
		}
		public void set_has_car(boolean c) {
			this.has_car=c;
		}
		public Car get_car() {
			return this.car;
		}
		public void set_car(Car c) {
			this.car=c;
		}
		public void set_pref_cat(char c) {
			this.pref_cat=c;
		}

		
		public Rent(Client k, String f, String t, char c,int a, int b) {			
			this.client=k;
			try {
				this.from_date = date_format.parse(f);
				this.to_date = date_format.parse(t);
			} 
			catch (java.text.ParseException e) {
				e.printStackTrace();
			}
			this.date_correct=false;
			this.pref_cat=c;
			this.has_car=false;
			this.km_cost=0;
			this.chair=a;
			this.bicycle_carrier=b;
			this.period = (to_date.getTime() - from_date.getTime())/1000/60/60/24 + 1;
			
		}
		
	}
		
	public static class Return {
		static int no_docs=500;
		static int no_wheel_trim=20;
		static int factor=3;
		
		private Rent rent;
		private Date return_date;
		private int mileage;
		private int missing_doc;
		private int lost_trims;
		
		private double total_cost;
		private long days_late;
		
		public Rent get_rent() {
			return this.rent;
		}
		public Date get_date() {
			return this.return_date;
		}
		public int get_punish_costs() {
			return this.missing_doc + this.lost_trims;
		}
		public double get_total_cost() {
			return this.total_cost;
		}
		public void set_total_cost(double c) {
			this.total_cost=c;
		}	
		public long get_days_late_costs() {
			return this.days_late;
		}
		public void set_days_late_costs(Date d,int day_cost) {
			this.days_late=(this.return_date.getTime()-d.getTime())/1000/60/60/24*factor*day_cost;
		}
		public int get_mileage() {
			return this.mileage;
		}
	    public Return(Rent rent, String date, int mil,int m_doc, int lost_tr) {
	        this.rent=rent;
	        try {
				this.return_date = date_format.parse(date);
			}
	        catch (java.text.ParseException e) {
				e.printStackTrace();
			}
	        this.total_cost=0;
	        this.mileage=mil;
			this.missing_doc=m_doc*no_docs;
			this.lost_trims=lost_tr*no_wheel_trim;
			this.days_late=0;
	    }   
	}
    public static final void main(String[] args) {
        try {
	        KieServices ks = KieServices.Factory.get();
    	    KieContainer kContainer = ks.getKieClasspathContainer();
        	KieSession kSession = kContainer.newKieSession("ksession-rules");
        	KieRuntimeLogger kLogger = ks.getLoggers().newFileLogger(kSession, "test");
            kSession.fireAllRules();
            kLogger.close();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

}

  

