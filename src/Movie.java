public class Movie {
    private final String name;
    private final String time;
    private final int price;

    public Movie(String name, String time, int price) {
        this.name = name;
        this.time = time;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public int getPrice() {
        return price;
    }

    public static java.util.List<Movie> getDefaultMovies() {
        return java.util.List.of(
                new Movie("Phir Hera Pheri", "5:00 PM", 220),
                new Movie("3 Idiots", "8:00 PM", 300),
                new Movie("Baby's Day Out", "6:00 PM", 350),
                new Movie("Chennai Express", "9:00 PM", 280),
                new Movie("Happy New Year", "9:00 PM", 250),
                new Movie("De Dana Dan", "9:00 PM", 280)


        );
    }
}
