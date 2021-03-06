package no.kristiania.taskManager.jdbc;

import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class ProjectDaoTest {

    private DataSource dataSource = TestDatabase.testDataSource();

    @Test
    void shouldFindSavedProject() throws SQLException{
        Project project = sampleProject();
        ProjectDao dao = new ProjectDao(dataSource);

        dao.insert(project);
        assertThat(dao.listAll()).contains(project);
        assertThat(dao.retrieve(project.getId())).isEqualToComparingFieldByField(project);
    }


    public static Project sampleProject(){
        Project project = new Project();
        project.setName(sampleProjectName());
        return project;
    }

    private static String sampleProjectName(){
        String[] projects = {"Project1", "Project2", "Project3"};
        return projects[new Random().nextInt(projects.length)];
    }

}
