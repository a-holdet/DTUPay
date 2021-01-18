package tokenservice.messagequeue;


import tokenservice.messagequeue.EventService;

public class StartUp {
    public static void main(String[] args) {
        EventService.getInstance();
    }
}
