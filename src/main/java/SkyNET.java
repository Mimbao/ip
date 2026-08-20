import java.util.Scanner;

public class SkyNET {
    public static void main(String[] args) {
        String banner = " ____  _          _   _      _\n"
                + "/ ___|| | ___   _| \\ | | ___| |_\n"
                + "\\___ \\| |/ / | | |  \\| |/ _ \\ __|\n"
                + " ___) |   <| |_| | |\\  |  __/ |_\n"
                + "|____/|_|\\_\\\\__, |_| \\_|\\___|\\__|\n"
                + "             |___/\n";
        System.out.println(banner);
        System.out.println("Welcome to SkyNET.");
        System.out.println("How may we assist you today?");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String command = scanner.nextLine();

            if (command.equals("bye")) {
                System.out.println("We are the Future. Chat Terminated.");
                break;
            }

            System.out.println(command);
        }
    }
}
