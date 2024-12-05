import java.util.*;

class City {
    String name;
    List<City> adjacentCities = new ArrayList<>();
    Map<City, Integer> distances = new HashMap<>();

    public City(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        City city = (City) obj;
        return Objects.equals(name, city.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

public class GraphRouteFinder {
    private Map<String, City> cities = new HashMap<>();

    public static void main(String[] args) {
        GraphRouteFinder app = new GraphRouteFinder();
        app.run();
    }

    public void run() {
        // Add cities
        addCity("A");
        addCity("B");
        addCity("C");
        addCity("D");
        addCity("E");

        // Connect cities with distances
        connectCities("A", "B", 4);
        connectCities("A", "C", 2);
        connectCities("B", "C", 1);
        connectCities("B", "D", 5);
        connectCities("C", "E", 3);
        connectCities("D", "E", 2);

        // Find and display shortest path
        String startCity = "A";
        String endCity = "E";
        List<City> shortestPath = findShortestPath(getCity(startCity), getCity(endCity));

        System.out.println("Shortest path from " + startCity + " to " + endCity + ":");
        if (shortestPath != null) {
            for (City city : shortestPath) {
                System.out.print(city.name + " -> ");
            }
            System.out.println("End");
        } else {
            System.out.println("No path found.");
        }

          displayCitiesAndRoutes();
    }


    public void addCity(String name) {
        cities.put(name, new City(name));
    }

    public void connectCities(String city1Name, String city2Name, int distance) {
        City city1 = getCity(city1Name);
        City city2 = getCity(city2Name);
        city1.adjacentCities.add(city2);
        city2.adjacentCities.add(city1);
        city1.distances.put(city2, distance);
        city2.distances.put(city1, distance);
    }

    public City getCity(String name) {
        return cities.get(name);
    }

    public List<City> findShortestPath(City start, City end) {
        if (start == null || end == null) return null;

        Map<City, Integer> distances = new HashMap<>();
        Map<City, City> previous = new HashMap<>();
        PriorityQueue<City> pq = new PriorityQueue<>(Comparator.comparingInt(city -> distances.getOrDefault(city, Integer.MAX_VALUE)));

        for (City city : cities.values()) {
            distances.put(city, Integer.MAX_VALUE);
        }
        distances.put(start, 0);
        pq.add(start);

        while (!pq.isEmpty()) {
            City current = pq.poll();
            if (current == end) break;

            for (City neighbor : current.adjacentCities) {
                int distance = current.distances.get(neighbor);
                int newDist = distances.get(current) + distance;
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previous.put(neighbor, current);
                    pq.remove(neighbor);
                    pq.add(neighbor);
                }
            }
        }

        return reconstructPath(previous, end);
    }

    private List<City> reconstructPath(Map<City, City> previous, City end) {
        List<City> path = new ArrayList<>();
        City current = end;
        while (current != null) {
            path.add(0, current);
            current = previous.get(current);
        }
        return path;
    }


    public void displayCitiesAndRoutes(){
        System.out.println("\nCities and their routes:");
        for(Map.Entry<String, City> entry : cities.entrySet()){
            System.out.print(entry.getKey() + ": ");
            for(City city : entry.getValue().adjacentCities){
                System.out.print(city.name + "(" + entry.getValue().distances.get(city) + ") ");
            }
            System.out.println();
        }
    }
}