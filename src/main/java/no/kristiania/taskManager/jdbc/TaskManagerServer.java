package no.kristiania.taskManager.jdbc;

import no.kristiania.taskManager.controllers.*;
import no.kristiania.taskManager.http.HttpServer;
import org.flywaydb.core.Flyway;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class TaskManagerServer {

    private HttpServer server;

    public TaskManagerServer(int port) throws IOException {
        Properties properties = new Properties();
        try (FileReader fileReader = new FileReader("task-manager.properties")) {
            properties.load(fileReader);
        }

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(properties.getProperty("dataSource.url"));
        dataSource.setUser(properties.getProperty("dataSource.user"));
        dataSource.setPassword(properties.getProperty("dataSource.password"));

        Flyway.configure().dataSource(dataSource).load().migrate();

        setUpTaskManagerServer(port, dataSource);
    }

    public TaskManagerServer(int port, DataSource dataSource) throws IOException {
        setUpTaskManagerServer(port, dataSource);
    }

    public void setUpTaskManagerServer(int port, DataSource dataSource) throws IOException {
        server = new HttpServer(port);
        server.setAssetRoot("src/main/resources/taskManager");
        server.addController("/api/listMembers", new ListMembersController(new MemberDao(dataSource)));
        server.addController("/api/listMembers", new ListMembersController(new MemberDao(dataSource)));
        server.addController("/api/listTasks", new ListTasksController(new TaskDao(dataSource)));
        server.addController("/api/listMemberships", new ListMembershipsController(new MembershipDao(dataSource), new MemberDao(dataSource), new TaskDao(dataSource)));
        server.addController("/api/addMember", new AddMemberController(new MemberDao(dataSource)));
        server.addController("/api/addTask", new AddTaskController(new TaskDao(dataSource)));
        server.addController("/api/addMembership", new AddMembershipController(new MembershipDao(dataSource), new MemberDao(dataSource), new TaskDao(dataSource)));
        server.addController("/api/updateMemberName", new UpdateMemberController(new MemberDao(dataSource)));
        server.addController("/api/updateTask",new UpdateTaskController(new TaskDao(dataSource)));
    }

    public static void main(String[] args) throws IOException {
        new TaskManagerServer(8080).start();
    }

    public void start() {
        server.start();
    }

    public int getPort() {
        return server.getPort();
    }
}
