import com.sun.net.httpserver.HttpServer;
import controllers.AppointmentHandler;
import controllers.ContactHandler;
import controllers.DocsHandler;
import controllers.ReportHandler;
import messaging.NotificationPublisher;
import repositories.AppointmentRepository;
import repositories.ContactRepository;
import services.AppointmentService;
import services.CepService;
import services.ContactService;
import services.ReportService;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class App {
    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(System.getenv().getOrDefault("APP_PORT", "8000"));

        ContactRepository contactRepository = new ContactRepository();
        AppointmentRepository appointmentRepository = new AppointmentRepository();
        CepService cepService = new CepService();
        NotificationPublisher notificationPublisher = new NotificationPublisher();
        ContactService contactService = new ContactService(contactRepository, cepService);
        AppointmentService appointmentService =
                new AppointmentService(appointmentRepository, contactRepository, notificationPublisher);
        ReportService reportService = new ReportService(port);

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/contacts", new ContactHandler(contactService));
        server.createContext("/appointments", new AppointmentHandler(appointmentService));
        server.createContext("/reports/agenda-summary", new ReportHandler(reportService));
        server.createContext("/openapi.json", DocsHandler.openapi());
        server.createContext("/swagger", DocsHandler.swagger());
        server.setExecutor(Executors.newFixedThreadPool(10));
        server.start();

        System.out.println("Agenda API running at http://localhost:" + port);
    }
}
