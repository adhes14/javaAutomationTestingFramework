package com.jalasoft.todoly.credentials;

import api.APIManager;
import entities.Project;
import framework.Environment;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DataProviderTests {
    private static final Environment environment = Environment.getInstance();
    private static final APIManager apiManager = APIManager.getInstance();

    @BeforeClass
    public void setup() {
        apiManager.setCredentials(environment.getUserName(), environment.getPassword());
    }

    @DataProvider(name = "data-provider")
    public Object[][] getData() {
        return new Object[][]
                {
                        {"3998252", "Personal"},
                        {"3998253", "Shopping List"},
                        {"4003660", "Project"}
                };
    }

    @Test(dataProvider = "data-provider")
    public void getProjectById(String projectId, String content) {
        System.out.println("Project Id is: " + projectId);

        String projectByIdEndpoint = environment.getProjectByIdEndpoint().replace("%d", projectId);
        Response response = apiManager.get(projectByIdEndpoint);
        Project responseProject = response.as(Project.class);

        Assert.assertEquals(response.getStatusCode(), 200, "Correct status code is not returned");
        Assert.assertTrue(response.getStatusLine().contains("200 OK"), "Correct status code and message is not returned");
        Assert.assertNull(response.jsonPath().getString("ErrorMessage"), "Error message was returned");
        Assert.assertNull(response.jsonPath().getString("ErrorCode"), "Error code was returned");
        Assert.assertEquals(responseProject.getId(), Integer.parseInt(projectId), "Id value is incorrect");
        Assert.assertEquals(responseProject.getContent(), content, "Content value is incorrect");
    }
}
