// Subsystem classes
class DVDPlayer {
    public void on() {
        System.out.println("DVD Player is on.");
    }

    public void play(String movie) {
        System.out.println("Playing movie: " + movie);
    }

    public void off() {
        System.out.println("DVD Player is off.");
    }
}

class Projector {
    public void on() {
        System.out.println("Projector is on.");
    }

    public void setInput(String input) {
        System.out.println("Projector input set to: " + input);
    }

    public void off() {
        System.out.println("Projector is off.");
    }
}

class SoundSystem {
    public void on() {
        System.out.println("Sound System is on.");
    }

    public void setVolume(int level) {
        System.out.println("Sound System volume set to: " + level);
    }

    public void off() {
        System.out.println("Sound System is off.");
    }
}

class Lights {
    public void dim(int level) {
        System.out.println("Lights dimmed to: " + level + "%");
    }

    public void on() {
        System.out.println("Lights are on.");
    }
}

// Facade class
class HomeTheaterFacade {
    private DVDPlayer dvdPlayer;
    private Projector projector;
    private SoundSystem soundSystem;
    private Lights lights;

    public HomeTheaterFacade(DVDPlayer dvdPlayer, Projector projector, SoundSystem soundSystem, Lights lights) {
        this.dvdPlayer = dvdPlayer;
        this.projector = projector;
        this.soundSystem = soundSystem;
        this.lights = lights;
    }

    public void watchMovie(String movie) {
        System.out.println("Get ready to watch a movie...");
        lights.dim(10);
        projector.on();
        projector.setInput("DVD");
        soundSystem.on();
        soundSystem.setVolume(5);
        dvdPlayer.on();
        dvdPlayer.play(movie);
    }

    public void endMovie() {
        System.out.println("Shutting movie theater down...");
        lights.on();
        projector.off();
        soundSystem.off();
        dvdPlayer.off();
    }
}

// Main class to demonstrate the Facade pattern
public class structuredemo2 {
    public static void main(String[] args) {
        DVDPlayer dvdPlayer = new DVDPlayer();
        Projector projector = new Projector();
        SoundSystem soundSystem = new SoundSystem();
        Lights lights = new Lights();

        HomeTheaterFacade homeTheater = new HomeTheaterFacade(dvdPlayer, projector, soundSystem, lights);

        // Watching a movie
        homeTheater.watchMovie("Inception");

        // Ending the movie
        homeTheater.endMovie();
    }
}