package com.jesus;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		Read_File access = new Read_File();
		
		
		if (args.length == 0 || args.length == 1) {
			throw new Exception("PATH Cannot be empty, please select a valid PATH file");
		}
		if (args[1].equalsIgnoreCase("CITY")) {
			if (args.length == 1) {
				throw new Exception("Invalid configuration, missing parameters");
			}
			if (args.length == 2) {
				throw new Exception("CITY Cannot be empty, please select a valid CITY name");
			} else if (args.length > 3) {
				throw new Exception("To many arguments, actually only support these format\n"
						+ "java -jar application.jar input.txt CITY (city-to-find)\n, sorry for the inconvenient");
			}
			ArrayList<String> print = access.filterStream(args[2], args[0]);
			ArrayList<People> returnList = access.readBufferResults(print);
			

			if (returnList.size() > 0) {
				access.writeBuffer(returnList, true);
				System.out.println("search completed successfully, please consult external file");
			} else if (returnList.size() == 0) {
				System.out.println("CITY don't find, please insert another :");
			}

		} else if (args[1].equalsIgnoreCase("ID")) {

			if (args.length == 2 || args[2].toCharArray().length <= 6 || args[2].toCharArray().length >= 11) {
				throw new Exception(
						"ID Cannot be empty, less than 6 digits or greater than 11 digits,\n please select a valid ID number");
			} else if (args.length > 3) {
				throw new Exception("To many arguments, actually only support these format\n"
						+ "java -jar application.jar input.txt ID (ID-to-find), sorry for the inconvenient");
			}
			ArrayList<String> print = access.filterStream(args[2], args[0]);
			ArrayList<People> returnList = access.readBufferResults(print);
			

			if (returnList.size() > 0) {
				access.writeBuffer(returnList, false);
				System.out.println("search completed successfully, please consult external file");
			} else if (returnList.size() == 0) {
				System.out.println("ID don't find, please insert another :");
			}

		} else if (args.length > 3) {
			throw new Exception("To many arguments, actually only support these format\n"
					+ "java -jar application.jar input.txt CITY (city-to-find)\n"
					+ "java -jar application.jar input.txt ID (ID-to-find), sorry for the inconvenient");
		}

	}
		
}

class People {
	private String name;
	private String id;
	private String city;
	public People() {
	}

	public People(String name, String city, String id) {
		this.name = name;
		this.id = id;
		this.city = city;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void show() {
		System.out.println("Name" + name);
		System.out.println("City" + city);
		System.out.println("ID" + id);
	}


	

}

class Read_File {
	

	public ArrayList<People> readBufferResults(ArrayList<String>list) {
		ArrayList<People>inputList=new ArrayList<People>();
		
			
			for(String input:list) {
				if(!input.isEmpty() && input.contains(",")) {
				StringTokenizer attribute = new StringTokenizer(input,",");
				while(attribute.hasMoreElements()) {
					People people = new People();
					people.setName(attribute.nextElement().toString().trim());
					people.setCity(attribute.nextElement().toString().trim());
					people.setId(attribute.nextElement().toString().trim());
					inputList.add(people);
				}
				
				
			
				} else if(!input.isEmpty() && input.contains(";")) {
					StringTokenizer attribute = new StringTokenizer(input,";");
					while(attribute.hasMoreElements()) {
						People people = new People();
						people.setName(attribute.nextElement().toString().trim());
						people.setCity(attribute.nextElement().toString().trim());
						people.setId(attribute.nextElement().toString().trim());
						inputList.add(people);
					}
					
					
					
					
				}
				
			}
			
			
		return inputList;
		

	}

	public ArrayList<String> filterStream(String city,String address) throws Exception {
		ArrayList<String> list = new ArrayList<>();
		
		try (Stream<String> stream = Files.lines(Paths.get(address))) {
			stream.filter(s -> s.contains(city.toLowerCase())
					|| s.contains(city.toUpperCase())
					|| s.contains(city.substring(0, city.length() - 1) +"-" + city.charAt(city.length() - 1))
					|| s.contains(city.substring(0, city.length() - 2) + city.charAt(city.length() - 1)))
					
					.forEach(list::add);
		
			stream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new Exception("Ups!! something has happened, need to provide a valid input file path \n"+e.getMessage());
		}
		
		return list;
	}

	public void writeBuffer(ArrayList<People> list,boolean mix) {

		BufferedWriter buffer = null;
		boolean print=true;
		String name = null;
		ArrayList<People> singleTime = validateOnlyTime(list);

		String address = "texto_salida";
		try {

			FileWriter outputFile = new FileWriter(address, true);

			buffer = new BufferedWriter(outputFile);
			if(mix) {
			for (People output : singleTime) {
				if(print) {
					buffer.newLine();
					buffer.write("a list of people and IDs that have been to "+ output.getCity());
					print=false;
				}
				name = output.getName().substring(1,output.getName().length());
				buffer.newLine();
				buffer.write(name + "," + output.getId());
				buffer.newLine();
				
				
				
			}
		}else {
             for (People output : singleTime) {
            	 if(print) {
            		buffer.newLine();
 					buffer.write("a list of cities that has been visited for: "+ output.getName().substring(1,output.getName().length()));
 					print=false;
 				}
            	 name = output.getName().substring(1,output.getName().length());
            	buffer.newLine();
				buffer.write( name +" "+output.getCity());
				buffer.newLine();
				
				

			}
		}

			buffer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public ArrayList<People> validateOnlyTime(ArrayList<People>list){
		
		ArrayList<People>outputList=new ArrayList<People>();
		
		outputList = (ArrayList<People>) list.stream()
				.filter(distinctByKeys(People::getCity,People::getId))
				.sorted(Comparator.comparing(People::getName))
				.distinct()
				.collect(Collectors.toList());
		
		return outputList;
	}

	private static <T> Predicate<T> 
    distinctByKeys(final Function<? super T, ?>... keyExtractors) 
{
    final Map<List<?>, Boolean> seen = new ConcurrentHashMap<>();
     
    return t -> 
    {
      final List<?> keys = Arrays.stream(keyExtractors)
                  .map(ke -> ke.apply(t))
                  .collect(Collectors.toList());
       
      return seen.putIfAbsent(keys, Boolean.TRUE) == null;
    };
}
	
	


	
	
}
