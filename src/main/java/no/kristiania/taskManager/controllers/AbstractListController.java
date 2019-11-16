package no.kristiania.taskManager.controllers;

import no.kristiania.taskManager.http.HttpServerResponse;
import no.kristiania.taskManager.http.HttpServerRequest;
import no.kristiania.taskManager.http.STATUS_CODE;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;

public abstract class AbstractListController<ENTITY> implements HttpController {

    protected ENTITY dao;

    public AbstractListController(ENTITY o) {
        this.dao = o;
    }

    public void handle(OutputStream outputStream, HttpServerRequest request) throws IOException, SQLException {
        HttpServerResponse response = new HttpServerResponse(request, outputStream);
        response.setHeader("Content-type", "text/html");

        try {
            response.setHeader("Content-length", Integer.toString(getBody().length()));
            response.setBody(getBody());
            response.executeResponse(STATUS_CODE.OK);
        } catch (SQLException e) {
            response.setHeader("Content-length", Integer.toString(e.toString().length()));
            response.setBody(e.toString());
            response.executeResponse(STATUS_CODE.INTERNAL_SERVER_ERROR);
        }
    }

    protected abstract String getBody() throws SQLException;

}
